package de.david.shopmanagement.presenter;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import de.david.shopmanagement.comparators.NodeNameComparator;
import de.david.shopmanagement.database.Neo4JConnector;
import de.david.shopmanagement.exceptions.MissingRootNodeException;
import de.david.shopmanagement.interfaces.ProductCatalogueModel;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Marvin
 */
public class ProductCataloguePresenterImpl implements ProductCataloguePresenter, ProductCatalogueView.ProductCatalogueViewListener {
    private static final String CONTAINER_PROPERTY = "name";
    private static final String CAPTION_TREE = "TreeCaption";
    private static final int SEARCH_PROPERTY_VALUE = 0;

    private ProductCatalogueModel productCatalogueModel;
    private ProductCatalogueView productCatalogueView;
    private GraphDatabaseService graphDb;
    private Neo4JConnector neo4JConnector;
    private Tree tree;
    private HierarchicalContainer container;

    public void init() {
        neo4JConnector = Neo4JConnector.getInstance();
        graphDb = Neo4JConnector.getInstance().getDatabaseService();
        createTreeNodes();

        productCatalogueView.addListener(this);

        productCatalogueView.setContentVisibility(false);
        productCatalogueView.hidePrice();
    }

    private void createTreeNodes() {
        container = new HierarchicalContainer();
        container.addContainerProperty(CONTAINER_PROPERTY, String.class, null);

        tree = new Tree(CAPTION_TREE);
        Label pc = neo4JConnector::getLabelProductcatalog;

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

        tree.setContainerDataSource(container);
        tree.setItemCaptionPropertyId(CONTAINER_PROPERTY);
        tree.setImmediate(true);

        productCatalogueView.createTree(tree);
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
        String contentName;
        String contentDescription;
        Double contentPrice = 0.d;

        try (Transaction tx = graphDb.beginTx()) {
            contentName = (String) node.getProperty(neo4JConnector.getNodePropertyName());
            contentDescription = (String) node.getProperty(neo4JConnector.getNodePropertyDescription());
            int childrenCount = 0;
            for (Relationship ignored : node.getRelationships(Direction.OUTGOING)) {
                childrenCount++;
            }
            if (childrenCount <= 0) {
                contentPrice = (double) node.getProperty(neo4JConnector.getNodePropertyPrice());
            }

            tx.success();
        }

        productCatalogueView.setContentNameTextField(contentName);
        productCatalogueView.setContentDescriptionTextField(contentDescription);
        if (contentPrice > 0) {
            productCatalogueView.showPrice();
            productCatalogueView.setContentPriceTextField(contentPrice.toString());
        } else {
            productCatalogueView.hidePrice();
        }
        if (!productCatalogueView.getContentLayout().isVisible()) {
            productCatalogueView.setContentVisibility(true);
        }
    }
}
