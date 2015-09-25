package de.david.shopmanagement.interfaces;

import com.vaadin.ui.Tree;
import de.david.shopmanagement.util.NodeData;
import org.neo4j.graphdb.Node;

/**
 * @author Marvin
 */
public interface ProductCatalogueModel {

    boolean saveNode(NodeData nodeData);

    boolean saveNodeProperty(Long nodeId, String nodeProperty, Object nodePropertyValue);

    void deleteNodeWithRelationships(Node node);

    void createTreeNodes();

    Tree getTreeNodes();

    void setTreeNodes(Tree treeNodes);

}
