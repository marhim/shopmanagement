package de.david.shopmanagement.interfaces;

import de.david.shopmanagement.util.CategoryData;

/**
 * Interface for the view of the main menu.
 *
 * @author Marvin
 */
public interface MainMenuView {

    MainMenuPresenter getPresenter();

    void setPresenter(MainMenuPresenter mainMenuPresenter);

    void addButton(CategoryData buttonData);
}
