package de.david.shopmanagement.model;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.comparators.NodeNameComparator;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.exceptions.MissingRootNodeException;
import de.david.shopmanagement.interfaces.ProductCatalogueModel;
import de.david.shopmanagement.util.NodeData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Marvin
 */
public class ProductCatalogueModelImpl implements ProductCatalogueModel {
    private static final Logger logger = LogManager.getLogger(ProductCatalogueModelImpl.class);
    private static final String CONTAINER_PROPERTY = "name";
    private static final String CAPTION_TREE = "TreeCaption";
    private static final int SEARCH_PROPERTY_VALUE = 0;

    private GraphDatabaseService graphDb;
    private Neo4JConnector neo4JConnector;
    private Tree treeNodes;
    private HierarchicalContainer container;

    public ProductCatalogueModelImpl() {
        neo4JConnector = Neo4JConnector.getInstance();
        graphDb = neo4JConnector.getDatabaseService();
    }

    @Override
    public boolean saveNode(NodeData nodeData) {
        boolean ret;
        Long nodeId = nodeData.getNodeId();
        String nodeName = nodeData.getNodeName();
        String nodeDescription = nodeData.getNodeDescription();
        Double nodePrice = nodeData.getNodePrice();

        try (Transaction tx = graphDb.beginTx()) {
            Node node = graphDb.getNodeById(nodeId);
            node.setProperty(neo4JConnector.getNodePropertyName(), nodeName);
            node.setProperty(neo4JConnector.getNodePropertyDescription(), nodeDescription);
            if (nodePrice != null && nodePrice >= 0) {
                node.setProperty(neo4JConnector.getNodePropertyPrice(), nodePrice);
            }

            ret = true;
            tx.success();
        } catch (Exception e) {
            ret = false;
        }

        return ret;
    }

    @Override
    public boolean saveNodeProperty(Long nodeId, String nodeProperty, Object nodePropertyValue) {
        boolean ret;

        try (Transaction tx = graphDb.beginTx()) {
            Node node = graphDb.getNodeById(nodeId);
            node.setProperty(nodeProperty, nodePropertyValue);

            ret = true;
            tx.success();
        } catch (Exception e) {
            ret = false;
        }

        return ret;
    }

    @Override
    public boolean deleteNodeWithRelationships(Node node) {
        boolean ret = false;

        try (Transaction tx = graphDb.beginTx()) {
            for (Relationship rel : node.getRelationships(Direction.INCOMING)) {
                rel.delete();
            }
            node.delete();

            ret = true;
            tx.success();
        } catch (TransactionFailureException e) {
            logger.error(e.getMessage());
        }

        return ret;
    }

    @Override
    public void createTreeNodes() {
        container = new HierarchicalContainer();
        container.addContainerProperty(CONTAINER_PROPERTY, String.class, null);

        treeNodes = new Tree(CAPTION_TREE);
        Label pc = neo4JConnector.getLabelProductcatalog();

        try (Transaction tx = graphDb.beginTx()) {
            Node rootNode = graphDb.findNode(pc, neo4JConnector.getNodePropertyIndex(), SEARCH_PROPERTY_VALUE);
            if (rootNode != null) {
                ArrayList<Node> rootChildren = new ArrayList<>();
                for (Relationship rootRelations : rootNode.getRelationships(Direction.OUTGOING)) {
                    rootChildren.add(rootRelations.getEndNode());
                }
                Collections.sort(rootChildren, new NodeNameComparator());
                for (Node rootChild : rootChildren) {
                    fillProductCatalogueTreeRek(null, rootChild);
                }
            } else {
                throw new MissingRootNodeException();
            }

            tx.success();
        } catch (MissingRootNodeException e) {
            e.printStackTrace();
        }

        treeNodes.setContainerDataSource(container);
        treeNodes.setItemCaptionPropertyId(CONTAINER_PROPERTY);
    }

    private void fillProductCatalogueTreeRek(Node parentNode, Node childNode) {
        addNodeToContainer(childNode);
        if (parentNode != null) {
            container.setParent(childNode, parentNode);
        }
        ArrayList<Node> children = new ArrayList<>();
        for (Relationship r : childNode.getRelationships(Direction.OUTGOING)) {
            children.add(r.getEndNode());
        }
        if (!children.isEmpty()) {
            Collections.sort(children, new NodeNameComparator());
            for (Node child : children) {
                fillProductCatalogueTreeRek(childNode, child);
            }
        } else {
            container.setChildrenAllowed(childNode, false);
        }
    }

    @SuppressWarnings("unchecked")
    private void addNodeToContainer(Node node) {
        Item item = container.addItem(node);
        // item is null if node is already in container
        if (item != null) {
            String captionName = (String) node.getProperty(neo4JConnector.getNodePropertyName());
            item.getItemProperty(CONTAINER_PROPERTY).setValue(captionName);
        }
    }

    @Override
    public Tree getTreeNodes() {
        return this.treeNodes;
    }

    @Override
    public void setTreeNodes(Tree treeNodes) {
        this.treeNodes = treeNodes;
    }

}
