package de.david.shopmanagement.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import de.david.shopmanagement.interfaces.StoreCataloguePresenter;
import de.david.shopmanagement.interfaces.StoreCatalogueView;

/**
 * @author Marvin
 */
public class StoreCatalogueViewImpl extends CustomComponent implements StoreCatalogueView, View {
    public static final String NAME = "StoreCatalogue";
    public static final String DISPLAY_NAME = "Filialkatalog";
    private StoreCataloguePresenter storeCataloguePresenter;
    private Layout layout;

    public StoreCatalogueViewImpl() {
        init();
    }

    private void init() {
        setSizeFull();

        layout = new VerticalLayout();
        Label label = new Label("Hallo FILIALKATALOG");
        layout.addComponent(label);

        setCompositionRoot(layout);
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
