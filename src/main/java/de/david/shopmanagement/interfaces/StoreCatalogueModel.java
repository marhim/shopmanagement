package de.david.shopmanagement.interfaces;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Tree;
import org.neo4j.graphdb.Node;

/**
 * @author Marvin
 */
public interface StoreCatalogueModel {

    void createStoreSelect();

    void createTreeFromStore(Node storeNode);

    ComboBox getStoreSelect();

    void setStoreSelect(ComboBox storeSelect);

    Tree getStoreTreeNodes();

    void setStoreTreeNodes(Tree storeTreeNodes);

}
