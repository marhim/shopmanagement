package de.david.shopmanagement.presenter;

import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.interfaces.StoreCatalogueModel;
import de.david.shopmanagement.interfaces.StoreCataloguePresenter;
import de.david.shopmanagement.interfaces.StoreCatalogueView;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Marvin
 */
public class StoreCataloguePresenterImpl implements StoreCataloguePresenter {
    private static final String PROPERTY_NAME = "name";

    private StoreCatalogueModel storeCatalogueModel;
    private StoreCatalogueView storeCatalogueView;
    private GraphDatabaseService graphDb;
    private Neo4JConnector neo4JConnector;
    private ComboBox storeSelect;
    private Node currentNode;

    public void init() {
        neo4JConnector = Neo4JConnector.getInstance();
        graphDb = neo4JConnector.getDatabaseService();
        createStoreSelect();
        storeSelect.addValueChangeListener(valueChangeEvent -> {
            currentNode = (Node) storeSelect.getValue();
            storeItemClick(currentNode);
        });

        storeCatalogueView.setTreePanelVisibility(false);
        storeCatalogueView.setContentVisibility(false);
    }

    private void createStoreSelect() {
        storeCatalogueModel.createStoreSelect();
        storeSelect = storeCatalogueModel.getStoreSelect();
        storeCatalogueView.setStoreComboBox(storeSelect);
    }

    private void storeItemClick(Node storeNode) {
        storeCatalogueView.setTreePanelVisibility(true);
    }

    @Override
    public StoreCatalogueModel getModel() {
        return storeCatalogueModel;
    }

    @Override
    public void setModel(StoreCatalogueModel storeCatalogueModel) {
        this.storeCatalogueModel = storeCatalogueModel;
    }

    @Override
    public StoreCatalogueView getView() {
        return storeCatalogueView;
    }

    @Override
    public void setView(StoreCatalogueView storeCatalogueView) {
        this.storeCatalogueView = storeCatalogueView;
    }
}
