package de.david.shopmanagement.exceptions;

import de.david.shopmanagement.util.Utility;

/**
 * This Exception will be thrown, if no root node could be found.
 *
 * @author Marvin
 */
public class MissingRootNodeException extends Exception {

    public MissingRootNodeException() {
        super(Utility.getInstance().getMissingRootNodeExceptionMessage());
    }
}
