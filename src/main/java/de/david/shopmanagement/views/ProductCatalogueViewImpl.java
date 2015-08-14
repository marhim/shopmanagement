package de.david.shopmanagement.views;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marvin
 */
public class ProductCatalogueViewImpl extends CatalogueViewImpl implements ProductCatalogueView, View, Tree.ValueChangeListener {
    public static final String NAME = "ProductCatalogue";
    public static final String DISPLAY_NAME = "Produktkatalog";
    private static final String CONTENT_TITLE = "Eigenschaften";
    private static final String CONTENT_NAME = "Name";
    private static final String CONTENT_DESCRIPTION = "Beschreibung";
    private static final String CONTENT_PRICE = "Preis";

    private List<ProductCatalogueViewListener> listeners = new ArrayList<>();
    private ProductCataloguePresenter productCataloguePresenter;
    private Label contentNameLabel;
    private Label contentDescriptionLabel;
    private Label contentPriceLabel;
    private TextField contentNameTextField;
    private TextArea contentDescriptionTextField;
    private TextField contentPriceTextField;
    private Tree tree;

    public ProductCatalogueViewImpl() {
        super();
        contentTitle.setValue(CONTENT_TITLE);
        contentNameLabel = new Label(CONTENT_NAME);
        contentDescriptionLabel = new Label(CONTENT_DESCRIPTION);
        contentPriceLabel = new Label(CONTENT_PRICE);
        contentNameTextField = new TextField();
        contentDescriptionTextField = new TextArea();
        contentPriceTextField = new TextField();
        init();
    }

    private void init() {
        setSizeFull();

        createMainMenuButton();
        createTitle(DISPLAY_NAME);
        createSplitPanel();

        createContent();

        addComponentsToMainLayout();

        setCompositionRoot(mainLayout);
    }

    public void createTree(Tree tree) {
        this.tree = tree;
        this.tree.addValueChangeListener(this);
        leftContentPanel.setContent(this.tree);
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

    public void setContentNameTextField(String nameTextField) {
        contentNameTextField.setValue(nameTextField);
    }

    public void setContentDescriptionTextField(String descriptionTextField) {
        contentDescriptionTextField.setValue(descriptionTextField);
    }

    public void setContentPriceTextField(String priceTextField) {
        contentPriceTextField.setValue(priceTextField);
    }

    public void showPrice() {
        setPriceVisibility(true);
    }

    public void hidePrice() {
        setPriceVisibility(false);
    }

    private void setPriceVisibility(boolean visibility) {
        contentPriceLabel.setVisible(visibility);
        contentPriceTextField.setVisible(visibility);
    }

    @Override
    public Layout getContentLayout() {
        return rightBodyLayout;
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
    public void addListener(ProductCatalogueViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        final Object id = tree.getValue();
        for (ProductCatalogueViewListener listener : listeners) {
            listener.treeItemClick((Node) id);
        }
    }
}
