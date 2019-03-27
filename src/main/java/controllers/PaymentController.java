package controllers;

import com.mercadopago.core.MPApiResponse;
import com.mercadopago.resources.Payment;
import controllers.handlers.*;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import services.PaymentsService;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class PaymentController {

    private final PaymentsService paymentsService;
    private final RequestHandlerFactory requestHandlerFactory;

    public PaymentController(PaymentsService paymentsService, RequestHandlerFactory requestHandlerFactory) {
        this.paymentsService = paymentsService;
        this.requestHandlerFactory = requestHandlerFactory;
    }

    /**
     *
     *
     * @param request Request
     * @param response Response
     * @return Map with response data or an ErrorResponse if the validation fails
     * @throws Exception
     */
    public Object processPaymentV1(Request request, Response response) throws Exception {
        return processPayment(requestHandlerFactory.getProcessPaymentV1Handler(request), response);
    }

    /**
     *
     *
     * @param request Request
     * @param response Response
     * @return Map with response data or an ErrorResponse if the validation fails
     * @throws Exception
     */
    public Object processPaymentV2(Request request, Response response) throws Exception {
        return processPayment(requestHandlerFactory.getProcessPaymentV2Handler(request), response);
    }

    private Object processPayment(ProcessPaymentRequestHandler requestHandler, Response response) throws Exception {
        response.header("Content-Type", "application/json");

        if(!requestHandler.isValid()) {
            response.status(HttpStatus.SC_BAD_REQUEST);
            return new ErrorResponse(HttpStatus.SC_BAD_REQUEST, ErrorMessages.BAD_REQUEST, ErrorMessages.INVALID_PAYMENT_PROCESSING, requestHandler.getInvalidCause());
        }

        Payment locPayment = paymentsService.save(requestHandler.getPaymentDTO());
        if(locPayment.getId() == null) {
            MPApiResponse mpa = locPayment.getLastApiResponse();
            response.status(mpa.getStatusCode());

            return mpa.getJsonElementResponse();
        }
        response.status(HttpStatus.SC_OK);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("payment_status", locPayment.getStatus());

        return responseMap;
    }

    /**
     *
     *
     * @param request Request
     * @param response Response
     * @return Map with response data
     * @throws Exception
     */
    public Object finishPaymentProcess(Request request, Response response) {
        response.header("Content-Type", "application/json");
        response.status(HttpStatus.SC_OK);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("payment_status", request.queryParams("payment_status"));
        responseMap.put("payment_status_detail", request.queryParams("payment_status_detail"));

        return responseMap;
    }

}
