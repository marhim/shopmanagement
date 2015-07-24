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
    private static final String CONTENT_TITLE = "Eigenschaften";
    private static final String CONTENT_NAME = "Name";
    private static final String CONTENT_DESCRIPTION = "Beschreibung";
    private static final String CONTENT_PRICE = "Preis";

    private ProductCataloguePresenter productCataloguePresenter;
    private Label contentNameLabel;
    private Label contentDescriptionLabel;
    private Label contentPriceLabel;
    private TextField contentNameTextField;
    private TextArea contentDescriptionTextField;
    private TextField contentPriceTextField;
    private Label leftLabel;

    public ProductCatalogueViewImpl() {
        super();
        contentTitle.setValue(CONTENT_TITLE);
        contentNameLabel = new Label(CONTENT_NAME);
        contentDescriptionLabel = new Label(CONTENT_DESCRIPTION);
        contentPriceLabel = new Label(CONTENT_PRICE);
        contentNameTextField = new TextField();
        contentDescriptionTextField = new TextArea();
        contentPriceTextField = new TextField();
        leftLabel = new Label("Ich bin links");
        init();
    }

    private void init() {
        setSizeFull();

        createMainMenuButton();
        createTitle(DISPLAY_NAME);
        createSplitPanel();

        leftBodyLayout.addComponent(leftLabel);
        createContent();

        addComponentsToMainLayout();

        setCompositionRoot(mainLayout);
    }

    private void createContent() {
        super.createContentTitle();
        rightBodyLayout.addComponent(contentNameLabel, 0, 1);
        rightBodyLayout.addComponent(contentNameTextField, 1, 1);
        rightBodyLayout.addComponent(contentDescriptionLabel, 0, 2);
        rightBodyLayout.addComponent(contentDescriptionTextField, 1, 2);
        rightBodyLayout.addComponent(contentPriceLabel, 0, 3);
        rightBodyLayout.addComponent(contentPriceTextField, 1, 3);
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
