package de.david.shopmanagement.interfaces;

/**
 * Interface for the presenter of the store catalogue.
 *
 * @author Marvin
 */
public interface StoreCataloguePresenter {

    void init();

    StoreCatalogueModel getModel();

    void setModel(StoreCatalogueModel storeCatalogueModel);

    StoreCatalogueView getView();

    void setView(StoreCatalogueView storeCatalogueView);

}
