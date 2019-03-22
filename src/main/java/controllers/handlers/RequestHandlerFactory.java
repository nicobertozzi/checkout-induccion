package controllers.handlers;

import spark.Request;

public class RequestHandlerFactory {

    /**
     * Handler for request solved by @see EndpointController
     *
     * @param request current request.
     * @return the correct handler for this request.
     */
    public CreatePreferenceRequestHandler getCreatePreferenceHandler(Request request) throws Exception {
        return new CreatePreferenceRequestHandler(request);
    }

    /**
     * Handler for request solved by @see EndpointController
     *
     * @param request current request.
     * @return the correct handler for this request.
     */
    public ProcessPaymentRequestHandler getProcessPaymentHandler(Request request) throws Exception {
        return new ProcessPaymentRequestHandler(request);
    }

}
