package de.david.shopmanagement.interfaces;

/**
 * Interface for the presenter of the product catalogue.
 *
 * @author Marvin
 */
public interface ProductCataloguePresenter {

    void init();

    ProductCatalogueModel getModel();

    void setModel(ProductCatalogueModel productCatalogueModel);

    ProductCatalogueView getView();

    void setView(ProductCatalogueView productCatalogueView);

}
