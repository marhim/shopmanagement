package de.david.shopmanagement;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import de.david.shopmanagement.interfaces.MainMenuView;
import de.david.shopmanagement.model.MainMenuModelImp;
import de.david.shopmanagement.presenter.MainMenuPresenterImp;
import de.david.shopmanagement.util.CategoryData;
import de.david.shopmanagement.views.MainMenuViewImp;

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
        storeCatalogue.setNavigatorName("StoreCatalogue"); // TODO: replace with constant, if ready
        storeCatalogue.setDisplayName("Filialkatalog");
        categories.add(storeCatalogue);
        CategoryData productCatalogue = new CategoryData();
        productCatalogue.setNavigatorName("ProductCatalogue"); // TODO: replace with constant, if ready
        productCatalogue.setDisplayName("Produktkatalog");
        categories.add(productCatalogue);

        MainMenuModelImp mainMenuModelImp = new MainMenuModelImp();
        mainMenuModelImp.addCategories(categories);
        MainMenuPresenterImp mainMenuPresenterImp = new MainMenuPresenterImp();
        mainMenuPresenterImp.setModel(mainMenuModelImp);
        MainMenuViewImp mainMenuViewImp = new MainMenuViewImp();
        mainMenuPresenterImp.setView(mainMenuViewImp);
        mainMenuPresenterImp.initMainMenubuttons();

        new Navigator(this, this);
        getNavigator().addView(MainMenuViewImp.NAME, mainMenuViewImp);
        getNavigator().navigateTo(MainMenuViewImp.NAME);
    }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
    }
}
