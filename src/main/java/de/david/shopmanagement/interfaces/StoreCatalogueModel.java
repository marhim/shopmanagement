package de.david.shopmanagement.interfaces;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Tree;
import org.neo4j.graphdb.Node;

/**
 * @author Marvin
 */
public interface StoreCatalogueModel {

    boolean saveRelationProperties(Node currentStore, Node node, String newShelf, Integer newAmount);

    void createStoreSelect();

    void createTreeFromStoreNode(Node storeNode);

    ComboBox getStoreSelect();

    void setStoreSelect(ComboBox storeSelect);

    Tree getStoreProductTree();

    void setStoreProductTree(Tree storeProductTree);

}
