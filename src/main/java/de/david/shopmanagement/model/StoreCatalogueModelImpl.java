package de.david.shopmanagement.model;

import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.interfaces.StoreCatalogueModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Marvin
 */
public class StoreCatalogueModelImpl implements StoreCatalogueModel {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONTAINER_PROPERTY = "name";
    private static final String STORE_SELECT_TITLE = "Filialauswahl";
    private static final String PLEASE_SELECT = "Bitte wählen...";

    private GraphDatabaseService graphDb;
    private Neo4JConnector neo4JConnector;
    private ComboBox storeSelect;
    private Tree storeProductTree;

    public StoreCatalogueModelImpl() {
        neo4JConnector = Neo4JConnector.getInstance();
        graphDb = neo4JConnector.getDatabaseService();
    }

    @Override
    public boolean saveRelationProperties(Node currentStore, Node node, String newShelf, Integer newAmount) {
        boolean ret = true;

        if (!(newShelf == null && newAmount == null)) {
            try (Transaction tx = graphDb.beginTx()) {
                for (Relationship r : node.getRelationships(Neo4JConnector.RelTypes.IS_SOLD_IN, Direction.OUTGOING)) {
                    if (r.getEndNode().getProperty(neo4JConnector.getNodePropertyIndex()).equals(currentStore.getProperty(neo4JConnector.getNodePropertyIndex()))) {
                        if (newShelf != null) {
                            r.setProperty(neo4JConnector.getNodePropertyShelf(), newShelf);
                        }
                        if (newAmount != null) {
                            r.setProperty(neo4JConnector.getNodePropertyAmount(), newAmount);
                        }
                    }
                }
                tx.success();
            } catch (Exception e) {
                logger.error(e.getMessage());
                ret = false;
            }
        } else {
            ret = false;
        }
        return ret;
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
    public void createTreeFromStoreNode(Node storeNode) {
        storeProductTree = new Tree();
        storeProductTree.addContainerProperty(CONTAINER_PROPERTY, String.class, null);
        storeProductTree.setItemCaptionPropertyId(CONTAINER_PROPERTY);
        int storeIndex = -1;

        try (Transaction tx = graphDb.beginTx()) {
            storeIndex = (int) storeNode.getProperty(neo4JConnector.getNodePropertyIndex());

            tx.success();
        }
        String pcLabelString = neo4JConnector.getLabelProductcatalog().toString();
        String storeLabelString = neo4JConnector.getLabelStore().toString();
        String productVariant = neo4JConnector.getNodeTypeProductvariant();
        String nodePropertyIndex = neo4JConnector.getNodePropertyIndex();
        String nodePropertyType = neo4JConnector.getNodePropertyType();
        String cypher = "MATCH (n:" + pcLabelString + ")-[r:" + Neo4JConnector.RelTypes.IS_SOLD_IN + "]->(m:" + storeLabelString
                + ") WHERE m." + nodePropertyIndex + "=" + storeIndex + " AND n." + nodePropertyType + "='" + productVariant + "' RETURN n";

        try (Transaction tx = graphDb.beginTx();
            Result result = graphDb.execute(cypher)) {
            Iterator<Node> n_column = result.columnAs("n");
            for (Node childNode : IteratorUtil.asIterable(n_column)) {
                addAllParentsToTreeRek(childNode, true);
            }

            tx.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void addAllParentsToTreeRek(Node childNode, boolean isVeryChild) {
        if (childNode != null) {
            addNodeToStoreProductTree(childNode);
            if (isVeryChild) {
                storeProductTree.setChildrenAllowed(childNode, false);
            }
            ArrayList<Node> parents = new ArrayList<>();
            for (Relationship r : childNode.getRelationships(Neo4JConnector.RelTypes.IS_PARENT_OF, Direction.INCOMING)) {
                parents.add(r.getStartNode());
            }
            if (parents.size() > 0) {
                for (Node parent : parents) {
                    if ((int) parent.getProperty(neo4JConnector.getNodePropertyIndex()) > 0) {
                        addAllParentsToTreeRek(parent, false);
                        storeProductTree.setParent(childNode, parent);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addNodeToStoreProductTree(Node node) {
        Item item = storeProductTree.addItem(node);
        // item is null if node is already in container
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
    public Tree getStoreProductTree() {
        return storeProductTree;
    }

    @Override
    public void setStoreProductTree(Tree storeTreeNodes) {
        this.storeProductTree = storeTreeNodes;
    }
}
