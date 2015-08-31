#!/usr/bin/env python3

import config.local

import logging
import random

from py2neo import Graph, Node, Relationship

logger = logging.getLogger('neo4j')
graph = Graph()

TRANSACTON_BULK_SIZE = 100


def bulk_create(data):
    for bulk_index in range(int(len(data) / TRANSACTON_BULK_SIZE) + 1):
        bulk = data[TRANSACTON_BULK_SIZE * bulk_index:TRANSACTON_BULK_SIZE * (bulk_index + 1)]
        graph.create(*bulk)


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

    logger.info('Creating nodes...')
    nodes = []

    root_node = create_node(index=0, level=0)
    nodes.append(root_node)

    for level in range(1, num_levels + 1):
        offset = sum(num_children ** l for l in range(level))
        for i in range(offset, offset + num_children ** level):
            nodes.append(create_node(index=i, level=level))

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


def create_stores():
    # https://en.wikipedia.org/wiki/List_of_United_States_cities_by_population
    all_store_names = [
        'New York City',
        'Los Angeles',
        'Chicago',
        'Houston',
        'Philadelphia',
        'Phoenix',
        'San Antonio',
        'San Diego',
        'Dallas',
        'San Jose',
        'Austin',
        'Jacksonville',
        'San Francisco',
        'Indianapolis',
        'Columbus',
        'Fort Worth',
        'Charlotte',
        'Detroit',
        'El Paso',
        'Seattle',
        'Denver',
        'Washington',
        'Memphis',
        'Boston',
        'Nashville',
        'Baltimore',
        'Oklahoma City',
        'Portland',
        'Las Vegas',
        'Louisville',
        'Milwaukee',
        'Albuquerque',
        'Tucson',
        'Fresno',
        'Sacramento',
        'Long Beach',
        'Kansas City',
        'Mesa',
        'Atlanta',
        'Virginia Beach',
        'Omaha',
        'Colorado Springs',
        'Raleigh',
        'Miami',
        'Oakland',
        'Minneapolis',
        'Tulsa',
        'Cleveland',
        'Wichita',
        'New Orleans',
        'Arlington',
        'Bakersfield',
        'Tampa',
        'Aurora',
        'Honolulu',
        'Anaheim',
        'Santa Ana',
        'Corpus Christi',
        'Riverside',
        'St',
        'Lexington',
        'Pittsburgh',
        'Stockton',
        'Anchorage',
        'Cincinnati',
        'Saint Paul',
        'Greensboro',
        'Toledo',
        'Newark',
        'Plano',
        'Henderson',
        'Lincoln',
        'Orlando',
        'Jersey City',
        'Chula Vista',
        'Buffalo',
        'Fort Wayne',
        'Chandler',
        'St',
        'Laredo',
        'Durham',
        'Irvine',
        'Madison',
        'Norfolk',
        'Lubbock',
        'Gilbert',
        'Winston',
        'Glendale',
        'Reno',
        'Hialeah',
        'Garland',
        'Chesapeake',
        'Irving',
        'North Las Vegas',
        'Scottsdale',
        'Baton Rouge',
        'Fremont',
        'Richmond',
        'Boise',
        'San Bernardino',
        'Birmingham',
        'Spokane',
        'Rochester',
        'Modesto',
        'Des Moines',
        'Oxnard',
        'Tacoma',
        'Fontana',
        'Fayetteville',
        'Moreno Valley',
        'Columbus',
        'Huntington Beach',
        'Yonkers',
        'Montgomery',
        'Aurora',
        'Glendale',
        'Shreveport',
        'Akron',
        'Little Rock',
        'Amarillo',
        'Augusta',
        'Mobile',
        'Grand Rapids',
        'Salt Lake City',
        'Huntsville',
        'Tallahassee',
        'Grand Prairie',
        'Overland Park',
        'Knoxville',
        'Brownsville',
        'Worcester',
        'Newport News',
        'Santa Clarita',
        'Providence',
        'Fort Lauderdale',
        'Garden Grove',
        'Oceanside',
        'Rancho Cucamonga',
        'Santa Rosa',
        'Port St',
        'Chattanooga',
        'Tempe',
        'Jackson',
        'Cape Coral',
        'Vancouver',
        'Ontario',
        'Sioux Falls',
        'Peoria',
        'Springfield',
        'Pembroke Pines',
        'Elk Grove',
        'Salem',
        'Corona',
        'Lancaster',
        'Eugene',
        'Palmdale',
        'McKinney',
        'Salinas',
        'Fort Collins',
        'Cary',
        'Hayward',
        'Springfield',
        'Pasadena',
        'Macon',
        'Pomona',
        'Alexandria',
        'Escondido',
        'Sunnyvale',
        'Lakewood',
        'Kansas City',
        'Rockford',
        'Torrance',
        'Hollywood',
        'Joliet',
        'Bridgeport',
        'Clarksville',
        'Paterson',
        'Naperville',
        'Frisco',
        'Mesquite',
        'Savannah',
        'Syracuse',
        'Dayton',
        'Pasadena',
        'Orange',
        'Fullerton',
        'McAllen',
        'Killeen',
        'Hampton',
        'Bellevue',
        'Warren',
        'Miramar',
        'West Valley City',
        'Olathe',
        'Columbia',
        'Sterling Heights',
        'Thornton',
        'New Haven',
        'Waco',
        'Charleston',
        'Thousand Oaks',
        'Visalia',
        'Cedar Rapids',
        'Elizabeth',
        'Roseville',
        'Gainesville',
        'Carrollton',
        'Stamford',
        'Denton',
        'Midland',
        'Coral Springs',
        'Concord',
        'Topeka',
        'Simi Valley',
        'Surprise',
        'Lafayette',
        'Kent',
        'Hartford',
        'Santa Clara',
        'Victorville',
        'Abilene',
        'Murfreesboro',
        'Evansville',
        'Vallejo',
        'Athens',
        'Allentown',
        'Berkeley',
        'Norman',
        'Ann Arbor',
        'Beaumont',
        'Independence',
        'Columbia',
        'Springfield',
        'El Monte',
        'Fargo',
        'Peoria',
        'Provo',
        'Lansing',
        'Odessa',
        'Downey',
        'Wilmington',
        'Arvada',
        'Costa Mesa',
        'Round Rock',
        'Carlsbad',
        'Miami Gardens',
        'Westminster',
        'Inglewood',
        'Rochester',
        'Fairfield',
        'Elgin',
        'West Jordan',
        'Clearwater',
        'Manchester',
        'Lowell',
        'Gresham',
        'Cambridge',
        'Ventura',
        'Temecula',
        'Waterbury',
        'Antioch',
        'Billings',
        'High Point',
        'Richardson',
        'Richmond',
        'West Covina',
        'Pueblo',
        'Murrieta',
        'Centennial',
        'Norwalk',
        'North Charleston',
        'Everett',
        'Pompano Beach',
        'Daly City',
        'Palm Bay',
        'Burbank',
        'Wichita Falls',
        'Boulder',
        'Green Bay',
        'Broken Arrow',
        'West Palm Beach',
        'College Station',
        'Pearland',
        'Santa Maria',
        'El Cajon',
        'San Mateo',
        'Lewisville',
        'Rialto',
        'Davenport',
        'Lakeland',
        'Clovis',
        'Sandy Springs',
        'Tyler',
        'Las Cruces',
        'South Bend'
    ]

    nodes = [
        Node('Store', index=all_store_names.index(store_name), name=store_name)
        for store_name
        in all_store_names
        ]
    graph.create(*nodes)


logger.info('Clearing data base...')
graph.delete_all()

create_product_catalog(num_levels=5, num_children=5)
create_stores()
