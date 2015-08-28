package de.david.shopmanagement.presenter;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.interfaces.ProductCatalogueModel;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;
import de.david.shopmanagement.util.NodeData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.cypher.EntityNotFoundException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Marvin
 */
public class ProductCataloguePresenterImpl implements ProductCataloguePresenter, ProductCatalogueView.ProductCatalogueViewListener {
    private static final Logger logger = LogManager.getLogger(ProductCataloguePresenterImpl.class);
    private static final String DEFAULT_NEW_NODE_NAME = "New Node";
    private static final Double DEFAULT_NEW_NODE_PRICE = 0.0d;
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
        tree.addValueChangeListener((Property.ValueChangeListener) productCatalogueView);

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
                    return new Action[] { ACTION_ADD_PRODUCT_GROUP, ACTION_ADD_PRODUCT};
                } else if (tree.areChildrenAllowed(target)) {
                    ArrayList<Action> actionList = new ArrayList<>();
                    actionList.add(ACTION_ADD_PRODUCT_GROUP);
                    actionList.add(ACTION_ADD_PRODUCT);
                    actionList.add(ACTION_ADD_PRODUCT_VARIANT);
                    if (!tree.hasChildren(target)) {
                        actionList.add(ACTION_REMOVE_ITEM);
                    }
                    Action[] ret = new Action[actionList.size()];
                    for (int i = 0; i < actionList.size(); i++) {
                        ret[i] = actionList.get(i);
                    }
                    return ret;
                } else {
                    return new Action[] { ACTION_REMOVE_ITEM };
                }
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == ACTION_ADD_PRODUCT) {
                    createNewNodeWithChildrenAllowed((Node) target, true);
                } else if (action == ACTION_ADD_PRODUCT_GROUP) {
                    createNewNodeWithChildrenAllowed((Node) target, true);
                } else if (action == ACTION_ADD_PRODUCT_VARIANT) {
                    createNewNodeWithChildrenAllowed((Node) target, false);
                } else if (action == ACTION_REMOVE_ITEM) {
                    tree.removeItem(target);
                    productCatalogueView.updateTree(tree);
                    productCatalogueView.setContentVisibility(false);
                    productCatalogueModel.deleteNodeWithRelationships((Node) target);
                    currentNode = null;
                }
            }
        });
    }

    private void createNewNodeWithChildrenAllowed(Node target, boolean childrenAllowed) {
        try (Transaction tx = graphDb.beginTx()) {
            Node newProductVariant = graphDb.createNode(neo4JConnector.getLabelProductcatalog());
            newProductVariant.setProperty(neo4JConnector.getNodePropertyIndex(), neo4JConnector.getNextIndex());
            newProductVariant.setProperty(neo4JConnector.getNodePropertyName(), DEFAULT_NEW_NODE_NAME);
            newProductVariant.setProperty(neo4JConnector.getNodePropertyDescription(), "");
            if (!childrenAllowed) {
                newProductVariant.setProperty(neo4JConnector.getNodePropertyPrice(), DEFAULT_NEW_NODE_PRICE);
            }
            target.createRelationshipTo(newProductVariant, Neo4JConnector.RelTypes.IS_PARENT_OF);

            Item newItem = tree.addItem(newProductVariant);
            newItem.getItemProperty(neo4JConnector.getNodePropertyName()).setValue(newProductVariant.getProperty(neo4JConnector.getNodePropertyName()));
            tree.setParent(newProductVariant, target);
            tree.setChildrenAllowed(newProductVariant, childrenAllowed);

            currentNode = newProductVariant;
            tree.select(newProductVariant);

            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (!neo4JConnector.hasChildren(currentNode) && !tree.areChildrenAllowed(currentNode)) {
                    contentPrice = (Double) currentNode.getProperty(neo4JConnector.getNodePropertyPrice());
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

    private boolean saveNode(Node node) {
        boolean ret = false;
        NodeData nodeData = new NodeData();
        try (Transaction tx = graphDb.beginTx()) {
            long nodeIdLong = node.getId();
            nodeData.setNodeId(nodeIdLong);

            ret = true;
            tx.success();
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage());
        }
        if (ret) {
            nodeData.setNodeName(productCatalogueView.getContentNameTextFieldValue());
            nodeData.setNodeDescription(productCatalogueView.getContentDescriptionTextAreaValue());
            try (Transaction tx = graphDb.beginTx()) {
                ret = false;
                if (node.hasProperty(neo4JConnector.getNodePropertyPrice()) && !node.getProperty(neo4JConnector.getNodePropertyPrice()).toString().isEmpty()) {
                    ret = nodeData.setNodePrice(productCatalogueView.getContentPriceTextFieldValue());
                }

                if (ret) {
                    tx.success();
                } else {
                    tx.failure();
                }
            }

            ret = productCatalogueModel.saveNode(nodeData);
        }

        return ret;
    }
}
