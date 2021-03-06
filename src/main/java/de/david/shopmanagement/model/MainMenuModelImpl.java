package de.david.shopmanagement.model;

import de.david.shopmanagement.interfaces.MainMenuModel;
import de.david.shopmanagement.util.CategoryData;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MainMenuModelImpl implements MainMenuModel {
    private List<CategoryData> categories;

    public MainMenuModelImpl() {
        this(null);
    }

    public MainMenuModelImpl(List<CategoryData> categories) {
        if (categories != null) {
            this.categories = categories;
        } else {
            this.categories = new ArrayList<>();
        }
    }

    @Override
    public List<CategoryData> getCategories() {
        return categories;
    }

    @Override
    public void addCategory(CategoryData categoryData) {
        if (categoryData != null) {
            categories.add(categoryData);
        }
    }

    @Override
    public void addCategories(List<CategoryData> categories) {
        if (categories != null) {
            this.categories = categories;
        }
    }
}
