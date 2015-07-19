package de.david.shopmanagement.presenter;

import de.david.shopmanagement.interfaces.ProductCatalogueModel;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;

/**
 * @author Marvin
 */
public class ProductCataloguePresenterImpl implements ProductCataloguePresenter {
    private ProductCatalogueModel productCatalogueModel;
    private ProductCatalogueView productCatalogueView;

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
}
