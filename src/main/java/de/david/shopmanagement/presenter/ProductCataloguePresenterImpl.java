package de.david.shopmanagement.presenter;

import com.vaadin.ui.Tree;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.interfaces.ProductCatalogueModel;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * @author Marvin
 */
public class ProductCataloguePresenterImpl implements ProductCataloguePresenter {
    private ProductCatalogueModel productCatalogueModel;
    private ProductCatalogueView productCatalogueView;
    private GraphDatabaseService graphDb;
    private Tree tree;

    public void init() {
        createTreeNodes();
    }

    private void createTreeNodes() {
        tree = new Tree("TreeCaption");
        graphDb = Neo4JConnector.getInstance().getDatabaseService();

        //TEST
        try (Transaction tx = graphDb.beginTx()) {
            int idToFind = 45;
            Node nodeFound = graphDb.getNodeById(idToFind);
            String nameofNode = (String) nodeFound.getProperty("name");
            tree.addItem(nameofNode);

            tx.success();
        }
        //TEST: end

        productCatalogueView.createTree(tree);
    }

    @Override
    public ProductCatalogueModel getModel() {
        return productCatalogueModel;
    }

    @Override
    public void setModel(ProductCatalogueModel productCatalogueModel) {
        this.productCatalogueModel = productCatalogueModel;
    }

    @Override
    public ProductCatalogueView getView() {
        return productCatalogueView;
    }

    @Override
    public void setView(ProductCatalogueView productCatalogueView) {
        this.productCatalogueView = productCatalogueView;
    }
}
