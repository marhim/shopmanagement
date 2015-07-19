package de.david.shopmanagement.model;

import de.david.shopmanagement.interfaces.StoreCatalogueModel;

import java.util.List;

/**
 * @author Marvin
 */
public class StoreCatalogueModelImpl implements StoreCatalogueModel {

    private List treeList;
    private List storeList;

    @Override
    public List getTreeList() {
        return this.treeList;
    }

    @Override
    public void setTreeList(List treeList) {
        this.treeList = treeList;
    }

    @Override
    public List getStoreList() {
        return this.storeList;
    }

    @Override
    public void setStoreList(List storeList) {
        this.storeList = storeList;
    }
}
