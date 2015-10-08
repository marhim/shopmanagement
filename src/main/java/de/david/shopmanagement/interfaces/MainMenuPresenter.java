package de.david.shopmanagement.interfaces;

/**
 * Interface for the presenter of the main menu.
 *
 * @author Marvin
 */
public interface MainMenuPresenter {

    MainMenuModel getModel();

    void setModel(MainMenuModel mainMenuModel);

    MainMenuView getView();

    void setView(MainMenuView mainMenuView);

    void initMainMenubuttons();
}
