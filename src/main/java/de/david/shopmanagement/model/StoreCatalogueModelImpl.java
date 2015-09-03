package de.david.shopmanagement.model;

import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.interfaces.StoreCatalogueModel;
import org.neo4j.graphdb.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Marvin
 */
public class StoreCatalogueModelImpl implements StoreCatalogueModel {
    private static final String CONTAINER_PROPERTY = "name";
    private static final String STORE_SELECT_TITLE = "Filialauswahl";
    private static final String PLEASE_SELECT = "Bitte wählen...";

    private GraphDatabaseService graphDb;
    private Neo4JConnector neo4JConnector;
    private ComboBox storeSelect;
    private Tree storeTreeNodes;

    public StoreCatalogueModelImpl() {
        neo4JConnector = Neo4JConnector.getInstance();
        graphDb = neo4JConnector.getDatabaseService();
    }

    @Override
    public void createStoreSelect() {
        storeSelect = new ComboBox();
        storeSelect.setCaption(STORE_SELECT_TITLE);
        storeSelect.setInputPrompt(PLEASE_SELECT);
        storeSelect.addContainerProperty(CONTAINER_PROPERTY, String.class, null);
        storeSelect.setItemCaptionPropertyId(CONTAINER_PROPERTY);
        Label storeLabel = neo4JConnector.getLabelStore();

        try (Transaction tx = graphDb.beginTx()) {
            ResourceIterator<Node> allStoreNodes = graphDb.findNodes(storeLabel);
            while(allStoreNodes.hasNext()) {
                addNodeToStoreSelect(allStoreNodes.next());
            }

            tx.success();
        }
    }

    @Override
    public void createTreeFromStore(Node storeNode) {

    }

    @SuppressWarnings("unchecked")
    private void addNodeToStoreSelect(Node node) {
        Item item = storeSelect.addItem(node);
        // item is null if node is already in storeSelect
        if (item != null) {
            String captionName = (String) node.getProperty(neo4JConnector.getNodePropertyName());
            item.getItemProperty(CONTAINER_PROPERTY).setValue(captionName);
        }
    }

    @Override
    public ComboBox getStoreSelect() {
        return storeSelect;
    }

    @Override
    public void setStoreSelect(ComboBox storeSelect) {
        this.storeSelect = storeSelect;
    }

    @Override
    public Tree getStoreTreeNodes() {
        return storeTreeNodes;
    }

    @Override
    public void setStoreTreeNodes(Tree storeTreeNodes) {
        this.storeTreeNodes = storeTreeNodes;
    }
}
