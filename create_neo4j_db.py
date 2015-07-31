import config.local

import logging
import random

from py2neo import Graph, Node, Relationship

logger = logging.getLogger('neo4j')
graph = Graph()

TRANSACTON_BULK_SIZE = 100


def bulk_create(data):
    for bulk_index in range(int(len(data) / TRANSACTON_BULK_SIZE) + 1):
        bulk = data[TRANSACTON_BULK_SIZE*bulk_index:TRANSACTON_BULK_SIZE*(bulk_index+1)]
        graph.create(*bulk)


def create_product_catalog(num_levels, num_children):

    def create_node(index):
        properties = {
            'index': index,
            'name': 'catalog item ' + str(index),
            'price': round(0.90 + 24.0 * random.random(), ndigits=2),
            'description': random.choice(['foo', 'bar', 'baz', 'glory'])
        }

        return Node('ProductCatalog', **properties)

    logger.info('Creating nodes...')
    nodes = []

    root_node = create_node(index=0)
    nodes.append(root_node)

    for level in range(1, num_levels + 1):
        offset = sum(num_children ** l for l in range(level))
        for i in range(offset, offset + num_children ** level):
            nodes.append(create_node(index=i))

    bulk_create(data=nodes)

    logger.info('Creating relations...')
    relations = []
    for node in nodes:
        if node == root_node:
            continue

        parent_index = int((node.properties['index'] - 1) / num_children)
        parent = graph.find_one('ProductCatalog', property_key='index', property_value=parent_index)

        # only leaves have prices
        parent['price'] = None

        parent_of = Relationship(parent, 'IS_PARENT_OF', node)
        relations.append(parent_of)

    # update nodes
    graph.push(*nodes)

    bulk_create(data=relations)

logger.info('Clearing data base...')
graph.delete_all()
create_product_catalog(num_levels=5, num_children=5)
