package de.david.shopmanagement.views;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import de.david.shopmanagement.interfaces.ProductCataloguePresenter;
import de.david.shopmanagement.interfaces.ProductCatalogueView;
import org.neo4j.graphdb.Node;

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
    private static final int TEXT_CHANGE_TIMEOUT = 5000;

    private List<ProductCatalogueViewListener> listeners = new ArrayList<>();
    private ProductCataloguePresenter productCataloguePresenter;
    private Label contentNameLabel;
    private Label contentDescriptionLabel;
    private Label contentPriceLabel;
    private TextField contentNameTextField;
    private TextArea contentDescriptionTextArea;
    private TextField contentPriceTextField;
    private Tree tree;

    public ProductCatalogueViewImpl() {
        super();
        contentTitle.setValue(CONTENT_TITLE);
        contentNameLabel = new Label(CONTENT_NAME);
        contentDescriptionLabel = new Label(CONTENT_DESCRIPTION);
        contentPriceLabel = new Label(CONTENT_PRICE);
        contentNameTextField = new TextField();
        contentDescriptionTextArea = new TextArea();
        contentPriceTextField = new TextField();
        init();
        config();
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

    private void config() {
        contentNameTextField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.TIMEOUT);
        contentNameTextField.setTextChangeTimeout(TEXT_CHANGE_TIMEOUT);

        contentDescriptionTextArea.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.TIMEOUT);
        contentDescriptionTextArea.setTextChangeTimeout(TEXT_CHANGE_TIMEOUT);

        contentPriceTextField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.TIMEOUT);
        contentPriceTextField.setTextChangeTimeout(TEXT_CHANGE_TIMEOUT);
    }

    public void setTree(Tree tree) {
        this.tree = tree;
        leftContentPanel.setContent(this.tree);
    }

    @Override
    public void updateTree(Tree tree) {
        this.tree = tree;
    }

    @Override
    public void setContentNameTextField(TextField contentNameTextField) {
        this.contentNameTextField = contentNameTextField;
    }

    @Override
    public TextField getContentNameTextField() {
        return contentNameTextField;
    }

    @Override
    public void setContentDescriptionTextArea(TextArea contentDescriptionTextArea) {
        this.contentDescriptionTextArea = contentDescriptionTextArea;
    }

    @Override
    public TextArea getContentDescriptionTextArea() {
        return contentDescriptionTextArea;
    }

    @Override
    public void setContentPriceTextField(TextField contentPriceTextField) {
        this.contentPriceTextField = contentPriceTextField;
    }

    @Override
    public TextField getContentPriceTextField() {
        return contentPriceTextField;
    }

    private void createContent() {
        super.createContentTitle();
        rightBodyLayout.addComponent(contentNameLabel, 0, 1);
        rightBodyLayout.addComponent(contentNameTextField, 1, 1);
        rightBodyLayout.addComponent(contentDescriptionLabel, 0, 2);
        rightBodyLayout.addComponent(contentDescriptionTextArea, 1, 2);
        rightBodyLayout.addComponent(contentPriceLabel, 0, 3);
        rightBodyLayout.addComponent(contentPriceTextField, 1, 3);
    }

    @Override
    public void setContentNameTextFieldValue(String nameTextFieldValue) {
        contentNameTextField.setValue(nameTextFieldValue);
    }

    @Override
    public String getContentNameTextFieldValue() {
        return contentNameTextField.getValue();
    }

    @Override
    public void setContentDescriptionTextAreaValue(String descriptionTextAreaValue) {
        contentDescriptionTextArea.setValue(descriptionTextAreaValue);
    }

    @Override
    public String getContentDescriptionTextAreaValue() {
        return contentDescriptionTextArea.getValue();
    }

    @Override
    public void setContentPriceTextFieldValue(String priceTextFieldValue) {
        contentPriceTextField.setValue(priceTextFieldValue);
    }

    @Override
    public String getContentPriceTextFieldValue() {
        return contentPriceTextField.getValue();
    }

    @Override
    public void showPrice() {
        setPriceVisibility(true);
    }

    @Override
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
        rightBodyLayout.setVisible(false);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        final Object id = tree.getValue();
        for (ProductCatalogueViewListener listener : listeners) {
            listener.treeItemClick((Node) id);
        }
    }
}
