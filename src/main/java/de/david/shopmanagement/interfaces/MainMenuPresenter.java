package de.david.shopmanagement.interfaces;

/**
 *
 */
public interface MainMenuPresenter {

    MainMenuModel getModel();

    void setModel(MainMenuModel mainMenuModel);

    MainMenuView getView();

    void setView(MainMenuView mainMenuView);

    void initMainMenubuttons();
}
