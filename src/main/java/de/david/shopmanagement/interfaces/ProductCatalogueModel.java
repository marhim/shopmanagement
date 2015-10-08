package de.david.shopmanagement.interfaces;

import com.vaadin.ui.Tree;
import de.david.shopmanagement.util.NodeData;
import org.neo4j.graphdb.Node;

/**
 * Interface for the model of the product catalogue.
 *
 * @author Marvin
 */
public interface ProductCatalogueModel {

    boolean saveNode(NodeData nodeData);

    /**
     * Save the property for a node with the given nodeId.
     *
     * @param nodeId Id for the node, which property should be saved
     * @param nodeProperty String for the name of the property
     * @param nodePropertyValue Value-object for the property
     * @return boolean, whether the query succeeded or failed
     */
    boolean saveNodeProperty(Long nodeId, String nodeProperty, Object nodePropertyValue);

    /**
     * Deletes all relationships for the given node and deletes the node itself afterwards.
     *
     * @param node Node to delete
     */
    void deleteNodeWithRelationships(Node node);

    /**
     * Searches the graph database for all nodes with Label 'ProductCatalog' and puts them in a Tree-Object.
     * The root node will be ignored, so it will not be listed in the Tree.
     * It throws a MissingRootNodeException if no root node is found.
     */
    void createTreeNodes();

    Tree getTreeNodes();

    void setTreeNodes(Tree treeNodes);

}
