package de.david.shopmanagement.presenter;

import com.vaadin.event.Action;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.interfaces.ProductCatalogueModel;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;
import de.david.shopmanagement.util.NodeData;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * @author Marvin
 */
public class ProductCataloguePresenterImpl implements ProductCataloguePresenter, ProductCatalogueView.ProductCatalogueViewListener {

    private static final Action ACTION_ADD_PRODUCT = new Action("Produkt hinzufügen");
    private static final Action ACTION_ADD_PRODUCT_GROUP = new Action("Produktgruppe hinzufügen");
    private static final Action ACTION_ADD_PRODUCT_VARIANT = new Action("Produktvariante hinzufügen");
    private static final Action ACTION_REMOVE_ITEM = new Action("Entfernen");

    private ProductCatalogueModel productCatalogueModel;
    private ProductCatalogueView productCatalogueView;
    private GraphDatabaseService graphDb;
    private Neo4JConnector neo4JConnector;
    private Tree tree;
    private Node currentNode;

    public void init() {
        neo4JConnector = Neo4JConnector.getInstance();
        graphDb = neo4JConnector.getDatabaseService();
        setTreeNodesView();

        productCatalogueView.addListener(this);

        productCatalogueView.setContentVisibility(false);
        productCatalogueView.hidePrice();

        addTextBlurListeners();
        addTextChangeListeners();
        addTreeActionHandler();
    }

    private void addTextBlurListeners() {
        productCatalogueView.getContentNameTextField().addBlurListener(blurEvent -> productCatalogueModel.saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyName(),
                productCatalogueView.getContentNameTextFieldValue()));

        productCatalogueView.getContentDescriptionTextArea().addBlurListener(blurEvent -> productCatalogueModel.saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyDescription(),
                productCatalogueView.getContentDescriptionTextAreaValue()));

        productCatalogueView.getContentPriceTextField().addBlurListener(blurEvent -> productCatalogueModel.saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyPrice(),
                productCatalogueView.getContentPriceTextFieldValue()));
    }

    private void addTextChangeListeners() {
        productCatalogueView.getContentNameTextField().addTextChangeListener(textChangeEvent -> productCatalogueModel.saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyName(),
                productCatalogueView.getContentNameTextFieldValue()));

        productCatalogueView.getContentDescriptionTextArea().addTextChangeListener(textChangeEvent -> productCatalogueModel.saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyDescription(),
                productCatalogueView.getContentDescriptionTextAreaValue()));

        productCatalogueView.getContentPriceTextField().addTextChangeListener(textChangeEvent -> productCatalogueModel.saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyPrice(),
                productCatalogueView.getContentPriceTextFieldValue()));
    }

    private void addTreeActionHandler() {
        tree.addActionHandler(new Action.Handler() {

            @Override
            public Action[] getActions(Object target, Object sender) {
                if (target == null) {
                    return new Action[] {ACTION_ADD_PRODUCT_GROUP, ACTION_ADD_PRODUCT};
                } else if (tree.areChildrenAllowed(target)) {
                    return new Action[] {ACTION_ADD_PRODUCT_GROUP, ACTION_ADD_PRODUCT, ACTION_ADD_PRODUCT_VARIANT, ACTION_REMOVE_ITEM };
                } else {
                    return new Action[] { ACTION_REMOVE_ITEM };
                }
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == ACTION_ADD_PRODUCT) {

                } else if (action == ACTION_ADD_PRODUCT_GROUP) {

                } else if (action == ACTION_ADD_PRODUCT_VARIANT) {

                } else if (action == ACTION_REMOVE_ITEM) {

                }
            }
        });
    }

    private void setTreeNodesView() {
        productCatalogueModel.createTreeNodes();
        tree = productCatalogueModel.getTreeNodes();

        productCatalogueView.setTree(tree);
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

    @Override
    public void treeItemClick(Node node) {
        if (node != null) {
            if (currentNode != null) {
                saveNode(currentNode);
            }
            currentNode = node;
            String contentName;
            String contentDescription;
            Double contentPrice = -1.d;

            try (Transaction tx = graphDb.beginTx()) {
                contentName = (String) currentNode.getProperty(neo4JConnector.getNodePropertyName());
                contentDescription = (String) currentNode.getProperty(neo4JConnector.getNodePropertyDescription());
                if (!neo4JConnector.hasChildren(currentNode)) {
                    contentPrice = (double) currentNode.getProperty(neo4JConnector.getNodePropertyPrice());
                }

                tx.success();
            }

            productCatalogueView.setContentNameTextFieldValue(contentName);
            productCatalogueView.setContentDescriptionTextAreaValue(contentDescription);
            if (contentPrice >= 0) {
                productCatalogueView.showPrice();
                productCatalogueView.setContentPriceTextFieldValue(contentPrice.toString());
            } else {
                productCatalogueView.hidePrice();
            }
            if (!productCatalogueView.getContentLayout().isVisible()) {
                productCatalogueView.setContentVisibility(true);
            }
        }
    }

    private void saveNode(Node node) {
        NodeData nodeData = new NodeData();
        try (Transaction tx = graphDb.beginTx()) {
            nodeData.setNodeId(node.getId());

            tx.success();
        }
        nodeData.setNodeName(productCatalogueView.getContentNameTextFieldValue());
        nodeData.setNodeDescription(productCatalogueView.getContentDescriptionTextAreaValue());
        try (Transaction tx = graphDb.beginTx()) {
            boolean isTxSuccessful = false;
            if (node.hasProperty(neo4JConnector.getNodePropertyPrice())) {
                isTxSuccessful = nodeData.setNodePrice(productCatalogueView.getContentPriceTextFieldValue());
            }

            if (isTxSuccessful) {
                tx.success();
            } else {
                tx.failure();
            }
        }

        productCatalogueModel.saveNode(nodeData);
    }
}
