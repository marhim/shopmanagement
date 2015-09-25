package de.david.shopmanagement.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.util.Map;

/**
 * @author Marvin
 */
public class Neo4JConnector {
    private static final Logger logger = LogManager.getLogger(Neo4JConnector.class);
    private static final Label LABEL_PRODUCTCATALOG = DynamicLabel.label("ProductCatalog");
    private static final Label LABEL_STORE = DynamicLabel.label("Store");
    private static final String NODE_PROPERTY_NAME = "name";
    private static final String NODE_PROPERTY_INDEX = "index";
    private static final String NODE_PROPERTY_DESCRIPTION = "description";
    private static final String NODE_PROPERTY_PRICE = "price";
    private static final String NODE_PROPERTY_TYPE = "type";
    private static final String NODE_PROPERTY_SHELF = "shelf";
    private static final String NODE_PROPERTY_AMOUNT = "amount";
    private static final String NODE_TYPE_PRODUCTGROUP = "productGroup";
    private static final String NODE_TYPE_PRODUCT = "product";
    private static final String NODE_TYPE_PRODUCTVARIANT = "productVariant";

    private static final String DB_PATH = "neo4j-database";
    private static GraphDatabaseService graphDb;

    public enum RelTypes implements RelationshipType {
        IS_PARENT_OF, IS_SOLD_IN
    }

    // Innere private Klasse, die erst beim Zugriff durch die umgebende Klasse initialisiert wird
    private static final class Neo4JInstanceHolder {
        // Initialisierung geschieht nur einmal und wird vom ClassLoader implizit synchronisiert
        static final Neo4JConnector INSTANCE = new Neo4JConnector();
    }

    private Neo4JConnector() {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        registerShutdownHook(graphDb);
    }

    public static Neo4JConnector getInstance() {
        return Neo4JInstanceHolder.INSTANCE;
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

    public String getNodePropertyType() {
        return NODE_PROPERTY_TYPE;
    }

    public String getNodeTypeProductgroup() {
        return NODE_TYPE_PRODUCTGROUP;
    }

    public String getNodeTypeProduct() {
        return NODE_TYPE_PRODUCT;
    }

    public String getNodeTypeProductvariant() {
        return NODE_TYPE_PRODUCTVARIANT;
    }

    public String getNodePropertyAmount() {
        return NODE_PROPERTY_AMOUNT;
    }

    public String getNodePropertyShelf() {
        return NODE_PROPERTY_SHELF;
    }

    public int getNextIndex() {
        int ret = -1;
        String resultString;
        String varName = "n";
        String cypherStatement = "MATCH (" + varName + ":ProductCatalog) RETURN max(" + varName + ".index)";
        try (Transaction tx = graphDb.beginTx();
             Result result = graphDb.execute(cypherStatement)) {
            while (result.hasNext()) {
                Map<String, Object> row = result.next();
                for (Map.Entry<String,Object> column : row.entrySet()) {
                    if (ret <= -1) {
                        ret = (int) column.getValue();
                    }
                }
            }
            resultString = result.resultAsString();
            tx.success();
        }
        if (ret > -1) {
            ret++;
        } else {
            logger.error("Next Index was not found. Index: " + ret + " Result as String: " + resultString);
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
