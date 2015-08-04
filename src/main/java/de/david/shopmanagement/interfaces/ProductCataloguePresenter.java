package de.david.shopmanagement.interfaces;

/**
 * @author Marvin
 */
public interface ProductCataloguePresenter {

    void init();

    ProductCatalogueModel getModel();

    void setModel(ProductCatalogueModel productCatalogueModel);

    ProductCatalogueView getView();

    void setView(ProductCatalogueView productCatalogueView);

}
