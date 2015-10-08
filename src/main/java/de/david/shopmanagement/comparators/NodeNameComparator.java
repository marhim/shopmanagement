package de.david.shopmanagement.comparators;

import de.david.shopmanagement.database.Neo4JConnector;
import org.neo4j.graphdb.Node;

import java.util.Comparator;

/**
 * Compares the name-property from two nodes as Strings.
 *
 * @author Marvin
 */
public class NodeNameComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2) {
        Neo4JConnector neo4JConnector = Neo4JConnector.getInstance();
        return ((String) n1.getProperty(neo4JConnector.getNodePropertyName())).compareTo((String) n2.getProperty(neo4JConnector.getNodePropertyName()));
    }

}
