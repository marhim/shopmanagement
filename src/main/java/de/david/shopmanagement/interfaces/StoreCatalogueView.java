package de.david.shopmanagement.interfaces;

import java.util.Collection;

/**
 * @author Marvin
 */
public interface StoreCatalogueView {

    void fillStoreSelect(Collection<String> storeStrings);

    StoreCataloguePresenter getPresenter();

    void setPresenter(StoreCataloguePresenter storeCataloguePresenter);

}
