package de.david.shopmanagement.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;

/**
 * @author Marvin
 */
public class ProductCatalogueViewImpl extends CustomComponent implements ProductCatalogueView, View {
    public static final String NAME = "ProductCatalogue";
    public static final String DISPLAY_NAME = "Produktkatalog";
    private ProductCataloguePresenter productCataloguePresenter;
    private Layout layout;

    public ProductCatalogueViewImpl() {
        init();
    }

    private void init() {
        setSizeFull();

        layout = new VerticalLayout();
        Label label = new Label("Hallo PRODUKTKATALOG");
        layout.addComponent(label);

        setCompositionRoot(layout);
    }

    @Override
    public ProductCataloguePresenter getPresenter() {
        return productCataloguePresenter;
    }

    @Override
    public void setPresenter(ProductCataloguePresenter productCataloguePresenter) {
        this.productCataloguePresenter = productCataloguePresenter;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
