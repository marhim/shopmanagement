package de.david.shopmanagement.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
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

    private StoreCataloguePresenter storeCataloguePresenter;
    private ComboBox storeSelect;
    private Label leftLabel;
    private Label rightLabel;

    public StoreCatalogueViewImpl() {
        super();
        init();
    }

    private void init() {
        setSizeFull();

        storeSelect = new ComboBox();
        leftLabel = new Label("Ich bin links");
        rightLabel = new Label("Ich bin rechts");

        createMainMenuButton();
        createTitle(DISPLAY_NAME);
        createStoreSelect();
        createSplitPanel();

        leftBodyLayout.addComponent(leftLabel);
        rightBodyLayout.addComponent(rightLabel);

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
