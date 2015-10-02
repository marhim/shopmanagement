package de.david.shopmanagement.views;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import de.david.shopmanagement.interfaces.CatalogueView;
import de.david.shopmanagement.util.Utility;

/**
 *
 */
public class CatalogueViewImpl extends CustomComponent implements CatalogueView {
    private static final String CAT_MAIN_MENU_BUTTON_STYLE = "catalog-main-menu-button";

    protected VerticalLayout mainLayout;
    protected HorizontalLayout headLayout;
    protected GridLayout rightBodyLayout;
    protected HorizontalSplitPanel bodySplitPanel;
    protected Panel leftContentPanel;
    protected Panel rightContentPanel;
    protected Button mainMenuButton;
    protected Label title;

    public CatalogueViewImpl() {
        mainLayout = new VerticalLayout();
        headLayout = new HorizontalLayout();
        bodySplitPanel = new HorizontalSplitPanel();
        leftContentPanel = new Panel(Utility.getInstance().getPanelLabelProductTree());
        rightContentPanel = new Panel(Utility.getInstance().getPanelLabelProperties());
        rightBodyLayout = new GridLayout(2,3);
        rightContentPanel.setContent(rightBodyLayout);
        initItemConfiguration();
    }

    protected void initItemConfiguration() {
        headLayout.setSizeFull();
        bodySplitPanel.setHeight(900, Unit.PIXELS);
        bodySplitPanel.setSplitPosition(20.0f, Unit.PERCENTAGE);
        bodySplitPanel.setLocked(true);
        leftContentPanel.setSizeFull();
        rightBodyLayout.setSpacing(true);
        rightBodyLayout.setMargin(true);
    }

    protected void createMainMenuButton() {
        mainMenuButton = new Button();
        mainMenuButton.setIcon(FontAwesome.TH);
        mainMenuButton.addStyleName(CAT_MAIN_MENU_BUTTON_STYLE);
        mainMenuButton.setDescription(MainMenuViewImpl.MAIN_MENU_DISPLAY_NAME);
        mainMenuButton.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(MainMenuViewImpl.NAME));
        headLayout.addComponent(mainMenuButton);
        headLayout.setComponentAlignment(mainMenuButton, Alignment.MIDDLE_LEFT);
    }

    protected void createTitle(String titleString) {
        title = new Label();
        title.setValue(titleString);
        headLayout.addComponent(title);
        headLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
    }

    protected void createSplitPanel() {
        bodySplitPanel.addComponent(leftContentPanel);
        bodySplitPanel.addComponent(rightContentPanel);
    }

    protected void addComponentsToMainLayout() {
        mainLayout.addComponent(headLayout);
        mainLayout.addComponent(bodySplitPanel);
    }

    @Override
    public void setContentVisibility(boolean visibility) {
        rightBodyLayout.setVisible(visibility);
        rightContentPanel.setVisible(visibility);
    }

    @Override
    public boolean isContentVisible() {
        return rightBodyLayout.isVisible();
    }

    @Override
    public void setTreePanelVisibility(boolean visibility) {
        leftContentPanel.setVisible(visibility);
    }

    @Override
    public boolean isTreePanelVisible() {
        return leftContentPanel.isVisible();
    }
}
