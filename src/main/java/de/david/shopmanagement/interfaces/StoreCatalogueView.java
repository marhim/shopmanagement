package de.david.shopmanagement.interfaces;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;

/**
 * Interface for the view of the store catalogue.
 *
 * @author Marvin
 */
public interface StoreCatalogueView {

    void setStoreComboBox(ComboBox storeSelect);

    void setTreePanelVisibility(boolean visibility);

    boolean isTreePanelVisible();

    void setContentVisibility(boolean visibility);

    boolean isContentVisible();

    void setStoreProductTree(Tree storeProductTree);

    void updateStoreProductTree(Tree storeProductTree);

    void hideQuantity();

    void showQuantity();

    void setContentNameTextFieldValue(String nameTextFieldValue);

    String getContentNameTextFieldValue();

    void setContentShelfNumberTextField(TextField shelfNumberTextField);

    TextField getContentShelfNumberTextField();

    void setContentShelfNumberTextFieldValue(String shelfNumberTextFieldValue);

    String getContentShelfNumberTextFieldValue();

    void setContentQuantityTextField(TextField quantityTextField);

    TextField getContentQuantityTextField();

    void setContentQuantityTextFieldValue(String quantityTextFieldValue);

    String getContentQuantityTextFieldValue();

    StoreCataloguePresenter getPresenter();

    void setPresenter(StoreCataloguePresenter storeCataloguePresenter);

}
