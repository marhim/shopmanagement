package de.david.shopmanagement.database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @author Marvin
 */
public class Neo4JConnector {
    private static final String LABEL_PRODUCTCATALOG = "ProductCatalog";
    private static final String LABEL_STORE = "Store";
    private static final String NODE_PROPERTY_NAME = "name";
    private static final String NODE_PROPERTY_INDEX = "index";
    private static final String NODE_PROPERTY_DESCRIPTION = "description";
    private static final String NODE_PROPERTY_PRICE = "price";

    private static final String DB_PATH = "neo4j-database";
    private static Neo4JConnector instance;
    private static GraphDatabaseService graphDb;

    public enum RelTypes implements RelationshipType {
        IS_PARENT_OF
    }

    private Neo4JConnector() {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        registerShutdownHook(graphDb);
    }

    public static Neo4JConnector getInstance() {
        if (Neo4JConnector.instance == null) {
            Neo4JConnector.instance = new Neo4JConnector();
        }
        return Neo4JConnector.instance;
    }

    public GraphDatabaseService getDatabaseService() {
        return Neo4JConnector.graphDb;
    }

    public String getLabelProductcatalog() {
        return LABEL_PRODUCTCATALOG;
    }

    public String getLabelStore() {
        return LABEL_STORE;
    }

    public String getNodePropertyName() {
        return NODE_PROPERTY_NAME;
    }

    public String getNodePropertyIndex() {
        return NODE_PROPERTY_INDEX;
    }

    public String getNodePropertyDescription() {
        return NODE_PROPERTY_DESCRIPTION;
    }

    public String getNodePropertyPrice() {
        return NODE_PROPERTY_PRICE;
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
