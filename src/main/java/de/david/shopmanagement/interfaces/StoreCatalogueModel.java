package de.david.shopmanagement.interfaces;

import java.util.List;

/**
 * @author Marvin
 */
public interface StoreCatalogueModel {

    List getTreeList();

    void setTreeList(List treeList);

    List getStoreList();

    void setStoreList(List storeList);

}
