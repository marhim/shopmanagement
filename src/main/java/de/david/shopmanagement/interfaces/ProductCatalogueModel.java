package de.david.shopmanagement.interfaces;

import com.vaadin.ui.Tree;
import de.david.shopmanagement.util.NodeData;

/**
 * @author Marvin
 */
public interface ProductCatalogueModel {

    boolean saveNode(NodeData nodeData);

    boolean saveNodeProperty(Long nodeId, String nodeProperty, Object nodePropertyValue);

    void createTreeNodes();

    Tree getTreeNodes();

    void setTreeNodes(Tree treeNodes);

}
