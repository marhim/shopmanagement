package de.david.shopmanagement.views;

import com.vaadin.ui.*;
import de.david.shopmanagement.interfaces.CatalogueView;

/**
 *
 */
public class CatalogueViewImpl extends CustomComponent implements CatalogueView {
    protected VerticalLayout mainLayout;
    protected HorizontalLayout headLayout;
    protected GridLayout rightBodyLayout;
    protected HorizontalSplitPanel bodySplitPanel;
    protected Panel leftContentPanel;
    protected Panel rightContentPanel;
    protected Button mainMenuButton;
    protected Label title;
    protected Label contentTitle;

    public CatalogueViewImpl() {
        mainLayout = new VerticalLayout();
        headLayout = new HorizontalLayout();
        bodySplitPanel = new HorizontalSplitPanel();
        leftContentPanel = new Panel();
        rightContentPanel = new Panel();
        rightBodyLayout = new GridLayout(2,4);
        rightContentPanel.setContent(rightBodyLayout);
        contentTitle = new Label();
        initItemConfiguration();
    }

    protected void initItemConfiguration() {
        headLayout.setSizeFull();
        bodySplitPanel.setHeight(900, Unit.PIXELS);
        bodySplitPanel.setSplitPosition(20.0f, Unit.PERCENTAGE);
        bodySplitPanel.setLocked(true);
        leftContentPanel.setSizeFull();
    }

    protected void createMainMenuButton() {
        mainMenuButton = new Button();
        mainMenuButton.setCaption(MainMenuViewImpl.MAIN_MENU_DISPLAY_NAME);
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

    protected void createContentTitle() {
        rightBodyLayout.addComponent(contentTitle, 0, 0, 1, 0);
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
