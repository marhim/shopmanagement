package de.david.shopmanagement.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import de.david.shopmanagement.interfaces.MainMenuPresenter;
import de.david.shopmanagement.interfaces.MainMenuView;
import de.david.shopmanagement.util.CategoryData;

/**
 *
 */
public class MainMenuViewImpl extends CustomComponent implements MainMenuView, View {
    public static final String NAME = "MainMenu";
    public static final String MAIN_MENU_DISPLAY_NAME = "Hauptmenü";
    private MainMenuPresenter mainMenuPresenter;
    private Layout buttonLayout;
    private VerticalLayout mainLayout;

    public MainMenuViewImpl() {
        init();
    }

    private void init() {
        setSizeFull();

        this.buttonLayout = new HorizontalLayout();
        this.buttonLayout.setCaption(MAIN_MENU_DISPLAY_NAME);
        this.buttonLayout.setSizeUndefined();

        this.mainLayout = new VerticalLayout(this.buttonLayout);
        this.mainLayout.setSizeFull();
        this.mainLayout.setComponentAlignment(this.buttonLayout, Alignment.MIDDLE_CENTER);
        setCompositionRoot(this.mainLayout);
    }

    @Override
    public void addButton(CategoryData buttonData) {
        Button tmpButton = new Button();
        tmpButton.setCaption(buttonData.getDisplayName());
        tmpButton.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(buttonData.getNavigatorName()));
        this.buttonLayout.addComponent(tmpButton);
    }

    @Override
    public MainMenuPresenter getPresenter() {
        return mainMenuPresenter;
    }

    @Override
    public void setPresenter(MainMenuPresenter mainMenuPresenter) {
        this.mainMenuPresenter = mainMenuPresenter;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
