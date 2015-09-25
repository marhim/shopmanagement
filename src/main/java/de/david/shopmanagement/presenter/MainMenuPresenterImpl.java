package de.david.shopmanagement.presenter;

import de.david.shopmanagement.interfaces.MainMenuModel;
import de.david.shopmanagement.interfaces.MainMenuPresenter;
import de.david.shopmanagement.interfaces.MainMenuView;
import de.david.shopmanagement.util.CategoryData;

import java.util.List;

/**
 *
 */
public class MainMenuPresenterImpl implements MainMenuPresenter {
    MainMenuModel mainMenuModel;
    MainMenuView mainMenuView;

    public MainMenuPresenterImpl() {
        super();
    }

    public MainMenuPresenterImpl(MainMenuModel mainMenuModel, MainMenuView mainMenuView) {
        this.mainMenuModel = mainMenuModel;
        this.mainMenuView = mainMenuView;
    }

    @Override
    public MainMenuModel getModel() {
        return mainMenuModel;
    }

    @Override
    public void setModel(MainMenuModel mainMenuModel) {
        this.mainMenuModel = mainMenuModel;
    }

    @Override
    public MainMenuView getView() {
        return mainMenuView;
    }

    @Override
    public void setView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
    }

    @Override
    public void initMainMenubuttons() {
        List<CategoryData> categories = this.mainMenuModel.getCategories();
        categories.forEach(mainMenuView::addButton);
    }

}
