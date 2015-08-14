package de.david.shopmanagement.interfaces;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Tree;
import org.neo4j.graphdb.Node;

/**
 * @author Marvin
 */
public interface ProductCatalogueView {

    void createTree(Tree tree);

    void setContentNameTextField(String nameTextField);

    void setContentDescriptionTextField(String descriptionTextField);

    void setContentPriceTextField(String priceTextField);

    void setContentVisibility(boolean visibility);

    void showPrice();

    void hidePrice();

    Layout getContentLayout();

    ProductCataloguePresenter getPresenter();

    void setPresenter(ProductCataloguePresenter productCataloguePresenter);

    interface ProductCatalogueViewListener {
        void treeItemClick(Node node);
    }

    void addListener(ProductCatalogueViewListener listener);

}
