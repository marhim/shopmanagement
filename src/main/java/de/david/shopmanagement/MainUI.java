package de.david.shopmanagement;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import de.david.shopmanagement.model.MainMenuModelImpl;
import de.david.shopmanagement.presenter.MainMenuPresenterImpl;
import de.david.shopmanagement.util.CategoryData;
import de.david.shopmanagement.views.MainMenuViewImpl;
import de.david.shopmanagement.views.ProductCatalogueViewImpl;
import de.david.shopmanagement.views.StoreCatalogueViewImpl;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Theme("maintheme")
@Widgetset("de.david.shopmanagement.MyAppWidgetset")
@Title("Shopmanagement")
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        List<CategoryData> categories = new ArrayList<>();
        CategoryData storeCatalogue = new CategoryData();
        storeCatalogue.setNavigatorName(StoreCatalogueViewImpl.NAME);
        storeCatalogue.setDisplayName(StoreCatalogueViewImpl.DISPLAY_NAME);
        categories.add(storeCatalogue);
        CategoryData productCatalogue = new CategoryData();
        productCatalogue.setNavigatorName(ProductCatalogueViewImpl.NAME);
        productCatalogue.setDisplayName(ProductCatalogueViewImpl.DISPLAY_NAME);
        categories.add(productCatalogue);

        MainMenuModelImpl mainMenuModelImpl = new MainMenuModelImpl();
        mainMenuModelImpl.addCategories(categories);
        MainMenuPresenterImpl mainMenuPresenterImpl = new MainMenuPresenterImpl();
        mainMenuPresenterImpl.setModel(mainMenuModelImpl);
        MainMenuViewImpl mainMenuViewImpl = new MainMenuViewImpl();
        mainMenuPresenterImpl.setView(mainMenuViewImpl);
        mainMenuPresenterImpl.initMainMenubuttons();

        new Navigator(this, this);
        getNavigator().addView(MainMenuViewImpl.NAME, mainMenuViewImpl);
        getNavigator().addView(StoreCatalogueViewImpl.NAME, StoreCatalogueViewImpl.class);
        getNavigator().addView(ProductCatalogueViewImpl.NAME, ProductCatalogueViewImpl.class);
        getNavigator().navigateTo(MainMenuViewImpl.NAME);
    }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
    }
}
