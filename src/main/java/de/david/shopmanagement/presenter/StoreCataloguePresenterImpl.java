package de.david.shopmanagement.presenter;

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

    public void init() {
        createStoreSelect();
    }

    private void createStoreSelect() {
        Collection<String> storeNames = new ArrayList<>();
        Label store = () -> Neo4JConnector.getInstance().getLabelStore();
        graphDb = Neo4JConnector.getInstance().getDatabaseService();

        try (Transaction tx = graphDb.beginTx()) {
            ResourceIterator<Node> allNodes = graphDb.findNodes(store);
            while (allNodes.hasNext()) {
                String storeName = (String) allNodes.next().getProperty(PROPERTY_NAME);
                storeNames.add(storeName);
            }

            tx.success();
        }

        storeCatalogueView.fillStoreSelect(storeNames);
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
