package de.david.shopmanagement.interfaces;

import com.vaadin.ui.Tree;

/**
 * @author Marvin
 */
public interface ProductCatalogueView {

    void createTree(Tree tree);

    ProductCataloguePresenter getPresenter();

    void setPresenter(ProductCataloguePresenter productCataloguePresenter);

}
