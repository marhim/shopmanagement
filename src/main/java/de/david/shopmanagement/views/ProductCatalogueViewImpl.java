package de.david.shopmanagement.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import de.david.shopmanagement.interfaces.CatalogueView;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;

/**
 * @author Marvin
 */
public class ProductCatalogueViewImpl extends CatalogueViewImpl implements ProductCatalogueView, View {
    public static final String NAME = "ProductCatalogue";
    public static final String DISPLAY_NAME = "Produktkatalog";
    private ProductCataloguePresenter productCataloguePresenter;
    private Label leftLabel;
    private Label rightLabel;

    public ProductCatalogueViewImpl() {
        super();
        init();
    }

    private void init() {
        setSizeFull();

        leftLabel = new Label("Ich bin links");
        rightLabel = new Label("Ich bin rechts");

        createMainMenuButton();
        createTitle(DISPLAY_NAME);
        createSplitPanel();

        leftBodyLayout.addComponent(leftLabel);
        rightBodyLayout.addComponent(rightLabel);

        addComponentsToMainLayout();

        setCompositionRoot(mainLayout);
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
