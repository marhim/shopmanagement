package de.david.shopmanagement.presenter;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.interfaces.StoreCatalogueModel;
import de.david.shopmanagement.interfaces.StoreCataloguePresenter;
import de.david.shopmanagement.interfaces.StoreCatalogueView;
import de.david.shopmanagement.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.*;

/**
 * @author Marvin
 */
public class StoreCataloguePresenterImpl implements StoreCataloguePresenter {
    private static final Logger logger = LogManager.getLogger(StoreCataloguePresenterImpl.class);

    private StoreCatalogueModel storeCatalogueModel;
    private StoreCatalogueView storeCatalogueView;
    private GraphDatabaseService graphDb;
    private Neo4JConnector neo4JConnector;
    private ComboBox storeSelect;
    private Tree storeProductTree;
    private Node currentStore;
    private Node currentNode;

    public void init() {
        neo4JConnector = Neo4JConnector.getInstance();
        graphDb = neo4JConnector.getDatabaseService();
        createStoreSelect();
        storeSelect.addValueChangeListener(valueChangeEvent -> {
            currentStore = (Node) storeSelect.getValue();
            if (currentStore != null) {
                storeItemClick(currentStore);
            }
        });

        storeCatalogueView.setTreePanelVisibility(false);
        storeCatalogueView.setContentVisibility(false);

        addTextBlurListeners();
        addTextChangeListeners();
    }

    private void addTextBlurListeners() {
        storeCatalogueView.getContentShelfNumberTextField().addBlurListener(blurEvent -> saveRelationProperty(
                storeCatalogueView.getContentShelfNumberTextFieldValue(),
                null
        ));
        storeCatalogueView.getContentQuantityTextField().addBlurListener(blurEvent -> saveRelationProperty(
                null,
                storeCatalogueView.getContentQuantityTextFieldValue()
        ));
    }

    private void addTextChangeListeners() {
        storeCatalogueView.getContentShelfNumberTextField().addTextChangeListener(textChangeEvent -> saveRelationProperty(
                storeCatalogueView.getContentShelfNumberTextFieldValue(),
                null
        ));
        storeCatalogueView.getContentQuantityTextField().addTextChangeListener(textChangeEvent -> saveRelationProperty(
                null,
                storeCatalogueView.getContentQuantityTextFieldValue()
        ));
    }

    private void createStoreSelect() {
        storeCatalogueModel.createStoreSelect();
        storeSelect = storeCatalogueModel.getStoreSelect();
        storeCatalogueView.setStoreComboBox(storeSelect);
    }

    private void storeItemClick(Node storeNode) {
        if (!storeCatalogueView.isTreePanelVisible()) {
            storeCatalogueView.setTreePanelVisibility(true);
        }
        if (storeCatalogueView.isContentVisible()) {
            storeCatalogueView.setContentVisibility(false);
        }
        storeCatalogueModel.createTreeFromStoreNode(storeNode);
        storeProductTree = storeCatalogueModel.getStoreProductTree();
        storeProductTree.addValueChangeListener(valueChangeEvent -> {
            Node node = (Node) storeProductTree.getValue();
            if (node != null) {
                storeProductTreeItemClick(node);
            }
        });
        storeCatalogueView.setStoreProductTree(storeProductTree);
    }

    private void storeProductTreeItemClick(Node node) {
        if (node != null) {
            currentNode = node;
            String contentName = "";
            String contentShelf = "";
            Integer contentAmount = -1;
            try (Transaction tx = graphDb.beginTx()) {
                contentName = (String) currentNode.getProperty(neo4JConnector.getNodePropertyName());
                for (Relationship r : currentNode.getRelationships(Neo4JConnector.RelTypes.IS_SOLD_IN, Direction.OUTGOING)) {
                    if (r.getEndNode().getProperty(neo4JConnector.getNodePropertyIndex()).equals(currentStore.getProperty(neo4JConnector.getNodePropertyIndex()))) {
                        contentShelf = (String) r.getProperty(neo4JConnector.getNodePropertyShelf());
                        if (currentNode.getProperty(neo4JConnector.getNodePropertyType()).toString().equals(neo4JConnector.getNodeTypeProductvariant())) {
                            contentAmount = (Integer) r.getProperty(neo4JConnector.getNodePropertyAmount());
                        }
                    }
                }

                tx.success();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            storeCatalogueView.setContentNameTextFieldValue(contentName);
            storeCatalogueView.setContentShelfNumberTextFieldValue(contentShelf);
            if (contentAmount >= 0) {
                storeCatalogueView.setContentQuantityTextFieldValue(contentAmount.toString());
                storeCatalogueView.showQuantity();
            } else {
                storeCatalogueView.hideQuantity();
            }
            if (!storeCatalogueView.isContentVisible()) {
                storeCatalogueView.setContentVisibility(true);
            }
        }
    }

    private boolean saveRelationProperty(String newShelf, String newAmount) {
        Integer newAmountInt = null;
        if (newAmount != null) {
            newAmountInt = Integer.parseInt(newAmount);
        }
        boolean ret = storeCatalogueModel.saveRelationProperties(currentStore, currentNode, newShelf, newAmountInt);
        if (ret) {
            Notification.show(Utility.getInstance().getPropertySaveSuccess(), Notification.Type.HUMANIZED_MESSAGE);
        } else {
            Notification.show(Utility.getInstance().getPropertySaveFailed(), Notification.Type.ERROR_MESSAGE);
        }
        return ret;
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
