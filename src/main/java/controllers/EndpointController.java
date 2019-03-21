package controllers;

import com.mercadopago.core.MPApiResponse;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import controllers.handlers.CreatePreferenceRequestHandler;
import controllers.handlers.ProcessPaymentRequestHandler;
import controllers.handlers.RequestHandlerFactory;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import services.PaymentsService;
import services.PreferencesService;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class EndpointController {

    private final PreferencesService preferencesService;
    private final PaymentsService paymentsService;
    private final RequestHandlerFactory requestHandlerFactory;

    public EndpointController(PreferencesService preferencesService, PaymentsService paymentsService, RequestHandlerFactory requestHandlerFactory) {
        this.preferencesService = preferencesService;
        this.paymentsService = paymentsService;

        this.requestHandlerFactory = requestHandlerFactory;
    }

    public Object createPreference(Request request, Response response) throws MPException {
        System.out.println("createPreference()");
        CreatePreferenceRequestHandler requestHandler = requestHandlerFactory.getCreatePreferenceHandler(request);

        if(!requestHandler.isValid()) {
            response.status(HttpStatus.SC_BAD_REQUEST);
            return new ErrorResponse(HttpStatus.SC_BAD_REQUEST, ErrorMessages.BAD_REQUEST, ErrorMessages.INVALID_PREFERENCE_CREATION, requestHandler.getInvalidCause());
        }

        Preference p = preferencesService.save(requestHandler.getPreferenceDTO());
        if(p.getId() == null) {
            MPApiResponse mpa = p.getLastApiResponse();
            response.status(mpa.getStatusCode());

            return mpa.getJsonElementResponse();
        }
        response.status(HttpStatus.SC_OK);

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("init_point", p.getInitPoint());

        return respMap;
    }

    public Object processPayment(Request request, Response response) throws MPException {
        System.out.println("processPayment()");
        ProcessPaymentRequestHandler requestHandler = requestHandlerFactory.getProcessPaymentHandler(request);

        if(!requestHandler.isValid()) {
            response.status(HttpStatus.SC_BAD_REQUEST);
            return new ErrorResponse(HttpStatus.SC_BAD_REQUEST, ErrorMessages.BAD_REQUEST, ErrorMessages.INVALID_PAYMENT_PROCESSING, requestHandler.getInvalidCause());
        }

        Payment p = paymentsService.save(requestHandler.getPaymentDTO());
        if(p.getId() == null) {
            MPApiResponse mpa = p.getLastApiResponse();
            response.status(mpa.getStatusCode());

            return mpa.getJsonElementResponse();
        }
        response.status(HttpStatus.SC_OK);

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("payment_status", p.getStatus());

        return respMap;
    }

    public Object finishPaymentProcess(Request request, Response response) {
        System.out.println("finishPaymentProcess()");

        try {
            System.out.println("payment status = " + request.queryParams("payment_status"));
            System.out.println("preference id = " + request.queryParams("preference_id"));
            System.out.println("back url = " + request.queryParams("back_url"));
            System.out.println("merchant order id = " + request.queryParams("merchant_order_id"));
            System.out.println("payment status detail = " + request.queryParams("payment_status_detail"));
            System.out.println("payment id = " + request.queryParams("payment_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("payment_status", request.queryParams("payment_status"));
        respMap.put("payment_status_detail", request.queryParams("payment_status_detail"));

        return respMap;
    }

}
