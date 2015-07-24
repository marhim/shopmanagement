package de.david.shopmanagement.views;

import com.vaadin.ui.*;
import de.david.shopmanagement.interfaces.CatalogueView;

/**
 *
 */
public class CatalogueViewImpl extends CustomComponent implements CatalogueView {
    protected Layout mainLayout;
    protected HorizontalLayout headLayout;
    protected Layout leftBodyLayout;
    protected GridLayout rightBodyLayout;
    protected HorizontalSplitPanel bodySplitPanel;
    protected Button mainMenuButton;
    protected Label title;
    protected Label contentTitle;

    public CatalogueViewImpl() {
        mainLayout = new VerticalLayout();
        headLayout = new HorizontalLayout();
        headLayout.setSizeFull();
        bodySplitPanel = new HorizontalSplitPanel();
        leftBodyLayout = new VerticalLayout();
        rightBodyLayout = new GridLayout(2,4);
        rightBodyLayout.setSizeFull();
        rightBodyLayout.setVisible(false);
        contentTitle = new Label();
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
        bodySplitPanel.setSplitPosition(20.0f, Unit.PERCENTAGE);
        bodySplitPanel.setLocked(true);
        bodySplitPanel.addComponent(leftBodyLayout);
        bodySplitPanel.addComponent(rightBodyLayout);
    }

    protected void addComponentsToMainLayout() {
        mainLayout.addComponent(headLayout);
        mainLayout.addComponent(bodySplitPanel);
    }
}
