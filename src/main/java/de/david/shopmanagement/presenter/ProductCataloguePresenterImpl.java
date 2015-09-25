package de.david.shopmanagement.presenter;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.interfaces.ProductCatalogueModel;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;
import de.david.shopmanagement.util.NodeData;
import de.david.shopmanagement.util.Utility;
import org.neo4j.cypher.EntityNotFoundException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marvin
 */
public class ProductCataloguePresenterImpl implements ProductCataloguePresenter, ProductCatalogueView.ProductCatalogueViewListener {
    private static final Logger logger = Logger.getLogger(ProductCataloguePresenterImpl.class.getName());
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
        productCatalogueView.getContentNameTextField().addBlurListener(blurEvent -> saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyName(),
                productCatalogueView.getContentNameTextFieldValue()));

        productCatalogueView.getContentDescriptionTextArea().addBlurListener(blurEvent -> saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyDescription(),
                productCatalogueView.getContentDescriptionTextAreaValue()));

        productCatalogueView.getContentPriceTextField().addBlurListener(blurEvent -> saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyPrice(),
                productCatalogueView.getContentPriceTextFieldValue()));
    }

    private void addTextChangeListeners() {
        productCatalogueView.getContentNameTextField().addTextChangeListener(textChangeEvent -> saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyName(),
                productCatalogueView.getContentNameTextFieldValue()));

        productCatalogueView.getContentDescriptionTextArea().addTextChangeListener(textChangeEvent -> saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyDescription(),
                productCatalogueView.getContentDescriptionTextAreaValue()));

        productCatalogueView.getContentPriceTextField().addTextChangeListener(textChangeEvent -> saveNodeProperty(
                currentNode.getId(),
                neo4JConnector.getNodePropertyPrice(),
                productCatalogueView.getContentPriceTextFieldValue()));
    }

    private void addTreeActionHandler() {
        tree.addActionHandler(new Action.Handler() {

            @Override
            public Action[] getActions(Object target, Object sender) {
                if (target == null) {
                    return new Action[]{ACTION_ADD_PRODUCT_GROUP};
                } else if (target instanceof Node) {
                    Node tmp = (Node) target;
                    String nodeType = null;
                    try (Transaction tx = graphDb.beginTx()) {
                        if (tmp.hasProperty(neo4JConnector.getNodePropertyType())) {
                            nodeType = tmp.getProperty(neo4JConnector.getNodePropertyType()).toString();
                        }
                        if (nodeType != null && !nodeType.isEmpty()) {
                            tx.success();
                        } else {
                            tx.failure();
                        }
                    }
                    Action[] ret;
                    if (nodeType != null && !nodeType.isEmpty()) {
                        ArrayList<Action> actionList = new ArrayList<>();
                        if (nodeType.equals(neo4JConnector.getNodeTypeProductgroup())) {
                            actionList.add(ACTION_ADD_PRODUCT_GROUP);
                            actionList.add(ACTION_ADD_PRODUCT);
                        } else if (nodeType.equals(neo4JConnector.getNodeTypeProduct())) {
                            actionList.add(ACTION_ADD_PRODUCT_VARIANT);
                        }
                        if (!tree.hasChildren(target)) {
                            actionList.add(ACTION_REMOVE_ITEM);
                        }
                        ret = new Action[actionList.size()];
                        for (int i = 0; i < actionList.size(); i++) {
                            ret[i] = actionList.get(i);
                        }
                    } else {
                        ret = new Action[]{ACTION_REMOVE_ITEM};
                    }
                    return ret;
                } else {
                    return new Action[]{ACTION_REMOVE_ITEM};
                }
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == ACTION_ADD_PRODUCT) {
                    //createNewNode((Node) target, neo4JConnector.getNodeTypeProduct());
                } else if (action == ACTION_ADD_PRODUCT_GROUP) {
                    //createNewNode((Node) target, neo4JConnector.getNodeTypeProductgroup());
                } else if (action == ACTION_ADD_PRODUCT_VARIANT) {
                    createNewNode((Node) target, neo4JConnector.getNodeTypeProductvariant());
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

    private void createNewNode(Node parent, String nodeType) {
        boolean isSuccessful = true;
        try (Transaction tx = graphDb.beginTx()) {
            Node newNode = graphDb.createNode(neo4JConnector.getLabelProductcatalog());
            newNode.setProperty(neo4JConnector.getNodePropertyIndex(), neo4JConnector.getNextIndex());
            newNode.setProperty(neo4JConnector.getNodePropertyName(), Utility.getInstance().getDefaultNewNodeName());
            productCatalogueView.setContentNameTextFieldValue(Utility.getInstance().getDefaultNewNodeName());
            newNode.setProperty(neo4JConnector.getNodePropertyDescription(), Utility.getInstance().getEmptyString());
            productCatalogueView.setContentDescriptionTextAreaValue(Utility.getInstance().getEmptyString());
            newNode.setProperty(neo4JConnector.getNodePropertyPrice(), DEFAULT_NEW_NODE_PRICE);
            newNode.setProperty(neo4JConnector.getNodePropertyType(), nodeType);

            parent.createRelationshipTo(newNode, Neo4JConnector.RelTypes.IS_PARENT_OF);

            Item newItem = tree.addItem(newNode);
            newItem.getItemProperty(neo4JConnector.getNodePropertyName()).setValue(newNode.getProperty(neo4JConnector.getNodePropertyName()));
            tree.setParent(newNode, parent);
            if (nodeType.equals(neo4JConnector.getNodeTypeProductvariant())) {
                productCatalogueView.setContentPriceTextFieldValue(DEFAULT_NEW_NODE_PRICE.toString());
                tree.setChildrenAllowed(newNode, false);
            } else {
                productCatalogueView.setContentPriceTextFieldValue("");
                tree.setChildrenAllowed(newNode, true);
            }

            currentNode = newNode;
            tree.select(newNode);

            tx.success();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            currentNode = null;
            isSuccessful = false;
        }

        if (isSuccessful) {
            Notification.show(Utility.getInstance().getNodeCreateSuccess(), Notification.Type.HUMANIZED_MESSAGE);
        } else {
            Notification.show(Utility.getInstance().getNodeCreateFailed(), Notification.Type.ERROR_MESSAGE);
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
                String nodeName;
                try (Transaction tx = graphDb.beginTx()) {
                    nodeName = (String) currentNode.getProperty(neo4JConnector.getNodePropertyName());

                    tx.success();
                }
                if (nodeName == null) {
                    nodeName = Utility.getInstance().getNodeNotFound();
                }
                if (saveNode(currentNode)) {
                    Notification.show(String.format(Utility.getInstance().getNodeSaveSuccess(), nodeName), Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    Notification.show(String.format(Utility.getInstance().getNodeSaveFailed(), nodeName), Notification.Type.ERROR_MESSAGE);
                }
            }
            currentNode = node;
            String contentName = "";
            String contentDescription = "";
            Double contentPrice = -1.d;

            try (Transaction tx = graphDb.beginTx()) {
                contentName = (String) currentNode.getProperty(neo4JConnector.getNodePropertyName());
                contentDescription = (String) currentNode.getProperty(neo4JConnector.getNodePropertyDescription());
                if (currentNode.getProperty(neo4JConnector.getNodePropertyType()).toString().equals(neo4JConnector.getNodeTypeProductvariant())) {
                    contentPrice = (Double) currentNode.getProperty(neo4JConnector.getNodePropertyPrice());
                }

                tx.success();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
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
        boolean ret = true;
        NodeData nodeData = new NodeData();
        try (Transaction tx = graphDb.beginTx()) {
            long nodeIdLong = node.getId();
            nodeData.setNodeId(nodeIdLong);

            tx.success();
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
            ret = false;
        }
        if (!ret) {
            nodeData.setNodeName(productCatalogueView.getContentNameTextFieldValue());
            nodeData.setNodeDescription(productCatalogueView.getContentDescriptionTextAreaValue());
            try (Transaction tx = graphDb.beginTx()) {
                ret = false;
                if (node.getProperty(neo4JConnector.getNodePropertyType()).toString().equals(neo4JConnector.getNodeTypeProductvariant())) {
                    ret = nodeData.setNodePrice(productCatalogueView.getContentPriceTextFieldValue());
                }

                if (ret) {
                    tx.success();
                } else {
                    tx.failure();
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
            }

            ret = productCatalogueModel.saveNode(nodeData);
        }

        return ret;
    }

    private boolean saveNodeProperty(Long nodeId, String nodeProperty, Object nodePropertyValue) {
        boolean ret = productCatalogueModel.saveNodeProperty(nodeId, nodeProperty, nodePropertyValue);
        if (ret) {
            Notification.show(Utility.getInstance().getPropertySaveSuccess(), Notification.Type.HUMANIZED_MESSAGE);
        } else {
            Notification.show(Utility.getInstance().getPropertySaveFailed(), Notification.Type.ERROR_MESSAGE);
        }
        return ret;
    }
}
