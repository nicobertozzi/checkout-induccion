package controllers.handlers;

import spark.Request;

public class RequestHandlerFactory {

    /**
     * Handler for request solved by @see EndpointController
     *
     * @param request current request.
     * @return the correct handler for this request.
     */
    public CreatePreferenceRequestHandler getCreatePreferenceHandler(Request request) {
        return new CreatePreferenceRequestHandler(request);
    }

    /**
     * Handler for request solved by @see EndpointController
     *
     * @param request current request.
     * @return the correct handler for this request.
     */
    public ProcessPaymentRequestHandler getProcessPaymentHandler(Request request) {
        return new ProcessPaymentRequestHandler(request);
    }

}
