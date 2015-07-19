package de.david.shopmanagement.model;

import de.david.shopmanagement.interfaces.ProductCatalogueModel;

import java.util.List;

/**
 * @author Marvin
 */
public class ProductCatalogueModelImpl implements ProductCatalogueModel {
    private List treeList;

    @Override
    public List getTreeList() {
        return this.treeList;
    }

    @Override
    public void setTreeList(List treeList) {
        this.treeList = treeList;
    }
}
