package de.david.shopmanagement.util;

/**
 * @author Marvin
 */
public class Utility {
    private static final String PANEL_LABEL_PRODUCT_TREE = "Produktbaum";
    private static final String PANEL_LABEL_PROPERTIES = "Eigenschaften";
    private static final String DEFAULT_NEW_NODE_NAME = "New Node";
    private static final String NODE_NOT_FOUND = "Node not found";
    private static final String STORE_INDEX_NOT_FOUND = "Store Index not found";
    private static final String EMPTY_STRING = "";
    private static final String NODE_CREATE_SUCCESS = "Neuer Knoten wurde erfolgreich erstellt!";
    private static final String NODE_CREATE_FAILED = "Neuer Knoten konnte nicht erstellt werden!";
    private static final String NODE_SAVE_SUCCESS = "Eigenschaften von '%s' wurden erfolgreich gespeichert!";
    private static final String NODE_SAVE_FAILED = "Eigenschaften von '%s' konnten nicht gespeichert werden!";
    private static final String PROPERTY_SAVE_SUCCESS = "Eigenschaft wurde erfolgreich gespeichert!";
    private static final String PROPERTY_SAVE_FAILED = "Eigenschaft konnte nicht gespeichert werden!";
    private static final String MISSING_ROOT_NODE_EXCEPTION_MESSAGE = "Es konnte kein Wurzelknoten ermittelt werden. Bitte überprüfen Sie die Suchparameter.";
    private static final int TEXT_CHANGE_TIMEOUT = 5000;

    // Innere private Klasse, die erst beim Zugriff durch die umgebende Klasse initialisiert wird
    private static final class UtilityInstanceHolder {
        // Initialisierung geschieht nur einmal und wird vom ClassLoader implizit synchronisiert
        static final Utility INSTANCE = new Utility();
    }

    private Utility() {}

    public static Utility getInstance() {
        return UtilityInstanceHolder.INSTANCE;
    }

    public String getPanelLabelProductTree() {
        return PANEL_LABEL_PRODUCT_TREE;
    }

    public String getPanelLabelProperties() {
        return PANEL_LABEL_PROPERTIES;
    }

    public String getDefaultNewNodeName() {
        return DEFAULT_NEW_NODE_NAME;
    }

    public String getNodeNotFound() {
        return NODE_NOT_FOUND;
    }

    public String getStoreIndexNotFound() {
        return STORE_INDEX_NOT_FOUND;
    }

    public String getEmptyString() {
        return EMPTY_STRING;
    }

    public String getPropertySaveSuccess() {
        return PROPERTY_SAVE_SUCCESS;
    }

    public String getPropertySaveFailed() {
        return PROPERTY_SAVE_FAILED;
    }

    public String getNodeCreateSuccess() {
        return NODE_CREATE_SUCCESS;
    }

    public String getNodeCreateFailed() {
        return NODE_CREATE_FAILED;
    }

    public String getNodeSaveSuccess() {
        return NODE_SAVE_SUCCESS;
    }

    public String getNodeSaveFailed() {
        return NODE_SAVE_FAILED;
    }

    public String getMissingRootNodeExceptionMessage() {
        return MISSING_ROOT_NODE_EXCEPTION_MESSAGE;
    }

    public int getTextChangeTimeout() {
        return TEXT_CHANGE_TIMEOUT;
    }
}