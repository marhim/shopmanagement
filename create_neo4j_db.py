#!/usr/bin/env python3

import config.local

import json
import logging
import random

from py2neo import Graph, Node, Relationship

logger = logging.getLogger('neo4j')
logger.setLevel(logging.INFO)
logger.addHandler(logging.StreamHandler())

graph = Graph()

TRANSACTON_BULK_SIZE = 100


class NodesAndRelations(object):
    def __init__(self):
        self.nodes = list()
        self.relations = list()


def bulk_create(data):
    data = list(data)
    for bulk_index in range(int(len(data) / TRANSACTON_BULK_SIZE) + 1):
        bulk = data[TRANSACTON_BULK_SIZE * bulk_index:TRANSACTON_BULK_SIZE * (bulk_index + 1)]
        graph.create(*bulk)


def by_index(index):
    return lambda node: node.properties['index'] == index


def create_product_catalog(num_levels, num_children):
    def create_node(index, level):
        if level == num_levels:
            node_type = 'productVariant'
        elif level == num_levels - 1:
            node_type = 'product'
        else:
            node_type = 'productGroup'

        properties = {
            'index': index,
            'name': 'catalog item ' + str(index),
            'type': node_type,
            'price': round(0.90 + 24.0 * random.random(), ndigits=2),
            'description': random.choice(['foo', 'bar', 'baz', 'glory'])
        }

        return Node('ProductCatalog', **properties)

    product_catalog = NodesAndRelations()

    logger.info('Creating product catalog nodes...')

    root_node = create_node(index=0, level=0)
    root_node.parent = None
    product_catalog.nodes.append(root_node)

    for level in range(1, num_levels + 1):
        offset = sum(num_children ** l for l in range(level))
        for i in range(offset, offset + num_children ** level):
            product_catalog.nodes.append(create_node(index=i, level=level))

    bulk_create(data=product_catalog.nodes)

    logger.info('Creating product catalog relations...')
    for node in product_catalog.nodes:
        if node == root_node:
            continue

        parent_index = int((node.properties['index'] - 1) / num_children)
        parent = next(filter(by_index(parent_index), product_catalog.nodes))

        # only leaves have prices
        parent['price'] = None

        parent_of = Relationship(parent, 'IS_PARENT_OF', node)
        product_catalog.relations.append(parent_of)

        node.parent = parent

    bulk_create(data=product_catalog.relations)

    return product_catalog


def create_stores():
    logger.info('Creating stores...')

    # https://en.wikipedia.org/wiki/List_of_United_States_cities_by_population
    with open('store_names.json') as store_names_file:
        all_store_names = json.load(store_names_file)

    nodes = list(
        Node('Store', index=all_store_names.index(store_name), name=store_name)
        for store_name
        in all_store_names
    )
    graph.create(*nodes)

    return nodes


logger.info('Clearing data base...')
graph.delete_all()

product_catalog = create_product_catalog(num_levels=5, num_children=5)
stores = create_stores()


def create_store_catalogs(product_catalog, stores):

    logger.info('Creating store catalog nodes...')
    for store in stores:
        store_catalog = NodesAndRelations()

        while len(store_catalog.nodes) < 0.1 * len(product_catalog.nodes):
            catalog_node = random.choice(product_catalog.nodes)

            while catalog_node:
                store_catalog.nodes.append(catalog_node)
                catalog_node = catalog_node.parent

        # remove duplicates
        store_catalog.nodes = list(set(store_catalog.nodes))

        for catalog_node in store_catalog.nodes:
            sold_in = Relationship(catalog_node, 'IS_SOLD_IN', store)
            store_catalog.relations.append(sold_in)

        bulk_create(data=store_catalog.relations)


create_store_catalogs(product_catalog=product_catalog, stores=stores)
