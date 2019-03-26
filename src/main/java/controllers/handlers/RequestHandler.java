package controllers.handlers;

import errors.ErrorCause;

import java.util.List;

public interface RequestHandler {

    /**
     * Method that must inform the status of the validation
     * @return whether the request is valid or not.
     */
    boolean isValid();

    /**
     * List validation failures in the request parameters.
     * @return a list of @see ErrorCause.java
     */
    List<ErrorCause> getInvalidCause();

}
