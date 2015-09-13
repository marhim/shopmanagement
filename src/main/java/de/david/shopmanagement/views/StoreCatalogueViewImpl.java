package de.david.shopmanagement.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import de.david.shopmanagement.interfaces.StoreCataloguePresenter;
import de.david.shopmanagement.interfaces.StoreCatalogueView;

import java.util.Collection;

/**
 * @author Marvin
 */
public class StoreCatalogueViewImpl extends CatalogueViewImpl implements StoreCatalogueView, View {
    public static final String NAME = "StoreCatalogue";
    public static final String DISPLAY_NAME = "Filialkatalog";
    private static final String CONTENT_TITLE = "Eigenschaften";
    private static final String CONTENT_NAME = "Name";
    private static final String CONTENT_SHELF_NUMBER = "Regalnummer";
    private static final String CONTENT_QUANTITY = "Stückzahl";

    private StoreCataloguePresenter storeCataloguePresenter;
    private ComboBox storeSelect;
    private Tree storeProductTree;
    private Label contentNameLabel;
    private Label contentShelfNumberLabel;
    private Label contentQuantityLabel;
    private TextField contentNameTextField;
    private TextField contentShelfNumberTextField;
    private TextField contentQuantityTextField;

    public StoreCatalogueViewImpl() {
        super();
        contentTitle.setValue(CONTENT_TITLE);
        contentNameLabel = new Label(CONTENT_NAME);
        contentShelfNumberLabel = new Label(CONTENT_SHELF_NUMBER);
        contentQuantityLabel = new Label(CONTENT_QUANTITY);
        contentNameTextField = new TextField();
        contentNameTextField.setEnabled(false);
        contentShelfNumberTextField = new TextField();
        contentQuantityTextField = new TextField();
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

    private void createContent() {
        super.createContentTitle();
        rightBodyLayout.addComponent(contentNameLabel, 0, 1);
        rightBodyLayout.addComponent(contentNameTextField, 1, 1);
        rightBodyLayout.addComponent(contentShelfNumberLabel, 0, 2);
        rightBodyLayout.addComponent(contentShelfNumberTextField, 1, 2);
        rightBodyLayout.addComponent(contentQuantityLabel, 0, 3);
        rightBodyLayout.addComponent(contentQuantityTextField, 1, 3);
    }

    @Override
    public void setStoreComboBox(ComboBox storeSelect) {
        this.storeSelect = storeSelect;
        headLayout.addComponent(this.storeSelect);
        headLayout.setComponentAlignment(this.storeSelect, Alignment.TOP_RIGHT);
    }

    @Override
    public void setStoreProductTree(Tree storeProductTree) {
        this.storeProductTree = storeProductTree;
        leftContentPanel.setContent(this.storeProductTree);
    }

    @Override
    public void updateStoreProductTree(Tree storeProductTree) {
        this.storeProductTree = storeProductTree;
    }

    @Override
    public void hideQuantity() {
        setQuantityVisibility(false);
    }

    @Override
    public void showQuantity() {
        setQuantityVisibility(true);
    }

    private void setQuantityVisibility(boolean visibility) {
        contentQuantityLabel.setVisible(visibility);
        contentQuantityTextField.setVisible(visibility);
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
    public void setContentShelfNumberTextFieldValue(String shelfNumberTextFieldValue) {
        contentShelfNumberTextField.setValue(shelfNumberTextFieldValue);
    }

    @Override
    public String getContentShelfNumberTextFieldValue() {
        return contentShelfNumberTextField.getValue();
    }

    @Override
    public void setContentQuantityTextFieldValue(String quantityTextFieldValue) {
        contentQuantityTextField.setValue(quantityTextFieldValue);
    }

    @Override
    public String getContentQuantityTextFieldValue() {
        return contentQuantityTextField.getValue();
    }

    @Override
    public StoreCataloguePresenter getPresenter() {
        return storeCataloguePresenter;
    }

    @Override
    public void setPresenter(StoreCataloguePresenter storeCataloguePresenter) {
        this.storeCataloguePresenter = storeCataloguePresenter;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}