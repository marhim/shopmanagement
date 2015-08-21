package de.david.shopmanagement.database;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.Iterator;

/**
 * @author Marvin
 */
public class Neo4JConnector {
    private static final Label LABEL_PRODUCTCATALOG = DynamicLabel.label("ProductCatalog");
    private static final Label LABEL_STORE = DynamicLabel.label("Store");
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

    public boolean hasChildren(Node node) {
        boolean ret = false;
        int childrenCount = 0;
        try (Transaction tx = graphDb.beginTx()) {
            for (Relationship ignored : node.getRelationships(Direction.OUTGOING)) {
                childrenCount++;
            }

            tx.success();
        }
        if (childrenCount > 0) {
            ret = true;
        }

        return ret;
    }

    public GraphDatabaseService getDatabaseService() {
        return Neo4JConnector.graphDb;
    }

    public Label getLabelProductcatalog() {
        return LABEL_PRODUCTCATALOG;
    }

    public Label getLabelStore() {
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

    public int getNextIndex() {
        int ret = -1;
        try (Transaction tx = graphDb.beginTx();
             Result result = graphDb.execute("MATCH (pc:ProductCatalog) RETURN pc ORDER BY pc.index DESC LIMIT 1")) {
            if (result.hasNext()) {
                Iterator<Node> n_column = result.columnAs("pc");
                for (Node node : IteratorUtil.asIterable(n_column)) {
                    ret = (int) node.getProperty(NODE_PROPERTY_INDEX);
                }
                ret++;
            }
        }
        return ret;
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
