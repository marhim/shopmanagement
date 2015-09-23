package de.david.shopmanagement.util;

/**
 * @author Marvin
 */
public class Utility {
    private static final String DEFAULT_NEW_NODE_NAME = "New Node";
    private static final String NODE_NOT_FOUND = "Node not found";
    private static final String EMPTY_STRING = "";
    private static final String NODE_SAVE_SUCCESS = "Eigenschaften von '%s' wurden erfolgreich gespeichert!";
    private static final String NODE_SAVE_FAILED = "Eigenschaften von '%s' konnten nicht gespeichert werden!";
    private static final String PROPERTY_SAVE_SUCCESS = "Eigenschaft wurde erfolgreich gespeichert!";
    private static final String PROPERTY_SAVE_FAILED = "Eigenschaft konnte nicht gespeichert werden!";
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

    public String getDefaultNewNodeName() {
        return DEFAULT_NEW_NODE_NAME;
    }

    public String getNodeNotFound() {
        return NODE_NOT_FOUND;
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

    public String getNodeSaveSuccess() {
        return NODE_SAVE_SUCCESS;
    }

    public String getNodeSaveFailed() {
        return NODE_SAVE_FAILED;
    }

    public int getTextChangeTimeout() {
        return TEXT_CHANGE_TIMEOUT;
    }
}