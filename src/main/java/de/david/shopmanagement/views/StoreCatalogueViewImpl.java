package de.david.shopmanagement.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import de.david.shopmanagement.interfaces.StoreCataloguePresenter;
import de.david.shopmanagement.interfaces.StoreCatalogueView;

/**
 * @author Marvin
 */
public class StoreCatalogueViewImpl extends CatalogueViewImpl implements StoreCatalogueView, View {
    public static final String NAME = "StoreCatalogue";
    public static final String DISPLAY_NAME = "Filialkatalog";
    private static final String STORE_SELECT_TITLE = "Filialauswahl";
    private static final String PLEASE_SELECT = "Bitte wählen...";
    private static final String CONTENT_TITLE = "Eigenschaften";
    private static final String CONTENT_NAME = "Name";
    private static final String CONTENT_SHELF_NUMBER = "Regalnummer";
    private static final String CONTENT_QUANTITY = "Stückzahl";

    private StoreCataloguePresenter storeCataloguePresenter;
    private ComboBox storeSelect;
    private Label contentNameLabel;
    private Label contentShelfNumberLabel;
    private Label contentQuantityLabel;
    private TextField contentNameTextField;
    private TextField contentShelfNumberTextField;
    private TextField contentQuantityTextField;

    private Label leftLabel;

    public StoreCatalogueViewImpl() {
        super();
        storeSelect = new ComboBox();
        contentTitle.setValue(CONTENT_TITLE);
        contentNameLabel = new Label(CONTENT_NAME);
        contentShelfNumberLabel = new Label(CONTENT_SHELF_NUMBER);
        contentQuantityLabel = new Label(CONTENT_QUANTITY);
        contentNameTextField = new TextField();
        contentShelfNumberTextField = new TextField();
        contentQuantityTextField = new TextField();
        leftLabel = new Label("Ich bin links");
        init();
    }

    private void init() {
        setSizeFull();

        createMainMenuButton();
        createTitle(DISPLAY_NAME);
        createStoreSelect();
        createSplitPanel();

        leftBodyLayout.addComponent(leftLabel);
        createContent();

        addComponentsToMainLayout();

        setCompositionRoot(mainLayout);
    }

    private void createStoreSelect() {
        storeSelect.setCaption(STORE_SELECT_TITLE);
        storeSelect.setInputPrompt(PLEASE_SELECT);
        storeSelect.addItem("Innenstadt");
        storeSelect.addItem("Außenstadt");
        storeSelect.addItem("Land");
        headLayout.addComponent(storeSelect);
        headLayout.setComponentAlignment(storeSelect, Alignment.TOP_RIGHT);
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
