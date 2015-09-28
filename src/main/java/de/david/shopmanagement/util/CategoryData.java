package de.david.shopmanagement.util;

import com.vaadin.server.Resource;

/**
 *
 */
public class CategoryData {
    private String navigatorName;
    private String displayName;
    private Resource icon;

    public String getNavigatorName() {
        return navigatorName;
    }

    public void setNavigatorName(String navigatorName) {
        this.navigatorName = navigatorName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Resource getIcon() {
        return icon;
    }

    public void setIcon(Resource icon) {
        this.icon = icon;
    }
}
