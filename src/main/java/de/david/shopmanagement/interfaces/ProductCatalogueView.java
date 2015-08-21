package de.david.shopmanagement.interfaces;

import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import org.neo4j.graphdb.Node;

/**
 * @author Marvin
 */
public interface ProductCatalogueView {

    void setTree(Tree tree);

    void setContentNameTextField(TextField contentNameTextField);

    TextField getContentNameTextField();

    void setContentDescriptionTextArea(TextArea contentDescriptionTextArea);

    TextArea getContentDescriptionTextArea();

    void setContentPriceTextField(TextField contentPriceTextField);

    TextField getContentPriceTextField();

    void setContentNameTextFieldValue(String nameTextFieldValue);

    String getContentNameTextFieldValue();

    void setContentDescriptionTextAreaValue(String descriptionTextAreaValue);

    String getContentDescriptionTextAreaValue();

    void setContentPriceTextFieldValue(String priceTextFieldValue);

    String getContentPriceTextFieldValue();

    void setContentVisibility(boolean visibility);

    void showPrice();

    void hidePrice();

    Layout getContentLayout();

    ProductCataloguePresenter getPresenter();

    void setPresenter(ProductCataloguePresenter productCataloguePresenter);

    interface ProductCatalogueViewListener {
        void treeItemClick(Node node);
    }

    void addListener(ProductCatalogueViewListener listener);

}
