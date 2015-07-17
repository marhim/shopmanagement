package de.david.shopmanagement.interfaces;

import de.david.shopmanagement.util.CategoryData;

/**
 *
 */
public interface MainMenuView {

    MainMenuPresenter getPresenter();

    void setPresenter(MainMenuPresenter mainMenuPresenter);

    void addButton(CategoryData buttonData);
}
