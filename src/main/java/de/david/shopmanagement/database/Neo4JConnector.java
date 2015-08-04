package de.david.shopmanagement.database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @author Marvin
 */
public class Neo4JConnector {
    private static final String DB_PATH = "neo4j-database";
    private static Neo4JConnector instance;
    private static GraphDatabaseService graphDb;

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

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
