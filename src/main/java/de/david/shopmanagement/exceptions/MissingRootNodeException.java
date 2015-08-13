package de.david.shopmanagement.exceptions;

/**
 * @author Marvin
 */
public class MissingRootNodeException extends Exception {
    private static final String MISSING_ROOT_NODE_EXCEPTION_MESSAGE = "Es konnte kein Wurzelknoten ermittelt werden. Bitte überprüfen Sie die Suchparameter.";

    public MissingRootNodeException() {
        super(MISSING_ROOT_NODE_EXCEPTION_MESSAGE);
    }
}
