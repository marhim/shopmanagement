package de.david.shopmanagement.interfaces;

import de.david.shopmanagement.util.CategoryData;

import java.util.List;

/**
 *
 */
public interface MainMenuModel {

    List getCategories();

    void addCategory(CategoryData categoryData);

    void addCategories(List<CategoryData> categories);
}
