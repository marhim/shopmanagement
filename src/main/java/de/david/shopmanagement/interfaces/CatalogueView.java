package de.david.shopmanagement.interfaces;

/**
 * Interface for the catalogue-view-superclass.
 */
public interface CatalogueView {

    void setContentVisibility(boolean visibility);

    boolean isContentVisible();

    void setTreePanelVisibility(boolean visibility);

    boolean isTreePanelVisible();

}
