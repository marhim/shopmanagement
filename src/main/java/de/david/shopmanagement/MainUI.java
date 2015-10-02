package de.david.shopmanagement;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import de.david.shopmanagement.interfaces.*;
import de.david.shopmanagement.model.MainMenuModelImpl;
import de.david.shopmanagement.model.ProductCatalogueModelImpl;
import de.david.shopmanagement.model.StoreCatalogueModelImpl;
import de.david.shopmanagement.presenter.MainMenuPresenterImpl;
import de.david.shopmanagement.presenter.ProductCataloguePresenterImpl;
import de.david.shopmanagement.presenter.StoreCataloguePresenterImpl;
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
        CategoryData productCatalogue = new CategoryData();
        productCatalogue.setNavigatorName(ProductCatalogueViewImpl.NAME);
        productCatalogue.setDisplayName(ProductCatalogueViewImpl.DISPLAY_NAME);
        productCatalogue.setIcon(FontAwesome.BOOK);
        categories.add(productCatalogue);
        CategoryData storeCatalogue = new CategoryData();
        storeCatalogue.setNavigatorName(StoreCatalogueViewImpl.NAME);
        storeCatalogue.setDisplayName(StoreCatalogueViewImpl.DISPLAY_NAME);
        storeCatalogue.setIcon(FontAwesome.BUILDING);
        categories.add(storeCatalogue);

        MainMenuModel mainMenuModel = new MainMenuModelImpl();
        mainMenuModel.addCategories(categories);
        MainMenuPresenter mainMenuPresenter = new MainMenuPresenterImpl();
        mainMenuPresenter.setModel(mainMenuModel);
        MainMenuViewImpl mainMenuViewImpl = new MainMenuViewImpl();
        mainMenuPresenter.setView(mainMenuViewImpl);
        mainMenuPresenter.initMainMenubuttons();

        ProductCatalogueModel productCatalogueModel = new ProductCatalogueModelImpl();
        ProductCataloguePresenter productCataloguePresenter = new ProductCataloguePresenterImpl();
        productCataloguePresenter.setModel(productCatalogueModel);
        ProductCatalogueViewImpl productCatalogueView = new ProductCatalogueViewImpl();
        productCataloguePresenter.setView(productCatalogueView);
        productCataloguePresenter.init();

        StoreCatalogueModel storeCatalogueModel = new StoreCatalogueModelImpl();
        StoreCataloguePresenter storeCataloguePresenter = new StoreCataloguePresenterImpl();
        storeCataloguePresenter.setModel(storeCatalogueModel);
        StoreCatalogueViewImpl storeCatalogueView = new StoreCatalogueViewImpl();
        storeCataloguePresenter.setView(storeCatalogueView);
        storeCataloguePresenter.init();

        new Navigator(this, this);
        getNavigator().addView(MainMenuViewImpl.NAME, mainMenuViewImpl);
        getNavigator().addView(ProductCatalogueViewImpl.NAME, productCatalogueView);
        getNavigator().addView(StoreCatalogueViewImpl.NAME, storeCatalogueView);
        getNavigator().navigateTo(MainMenuViewImpl.NAME);
    }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
    }
}
