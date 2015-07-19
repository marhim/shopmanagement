package de.david.shopmanagement.presenter;

import de.david.shopmanagement.interfaces.StoreCatalogueModel;
import de.david.shopmanagement.interfaces.StoreCataloguePresenter;
import de.david.shopmanagement.interfaces.StoreCatalogueView;

/**
 * @author Marvin
 */
public class StoreCataloguePresenterImpl implements StoreCataloguePresenter {
    private StoreCatalogueModel storeCatalogueModel;
    private StoreCatalogueView storeCatalogueView;

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
