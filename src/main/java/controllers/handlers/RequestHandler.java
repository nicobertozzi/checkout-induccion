package controllers.handlers;

import errors.ErrorCause;

import java.util.List;

public interface RequestHandler {

    /**
     * This method must only return valid parameters sent in the body, validated by the RequestHandler.
     * @param parameter the searched parameter.
     * @param <T> type of the parameter.
     * @return a valid body parameter.
     */
    <T> T getValidParam(String parameter);

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
