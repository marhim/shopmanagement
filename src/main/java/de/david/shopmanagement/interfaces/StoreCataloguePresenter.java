package de.david.shopmanagement.interfaces;

/**
 * @author Marvin
 */
public interface StoreCataloguePresenter {

    void init();

    StoreCatalogueModel getModel();

    void setModel(StoreCatalogueModel storeCatalogueModel);

    StoreCatalogueView getView();

    void setView(StoreCatalogueView storeCatalogueView);

}
