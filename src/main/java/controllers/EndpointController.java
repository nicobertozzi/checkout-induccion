package controllers;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import controllers.handlers.CreatePreferenceRequestHandler;
import controllers.handlers.RequestHandler;
import controllers.handlers.RequestHandlerFactory;
import errors.ErrorMessages;
import errors.ErrorResponse;
import model.PreferenceModel;
import org.apache.http.HttpStatus;
import services.PreferencesService;
import spark.Request;
import spark.Response;
import utils.RequestUtil;

public class EndpointController {

    private final PreferencesService preferencesService;
    private final RequestHandlerFactory requestHandlerFactory;

    public EndpointController(PreferencesService preferencesService, RequestHandlerFactory requestHandlerFactory) {
        this.preferencesService = preferencesService;
        this.requestHandlerFactory = requestHandlerFactory;
    }

    public Object createPreference(Request request, Response response) throws MPException {
        CreatePreferenceRequestHandler requestHandler = requestHandlerFactory.getCreatePreferenceHandler(request);

        if(!requestHandler.isValid()) {
            response.status(HttpStatus.SC_BAD_REQUEST);
            return new ErrorResponse(HttpStatus.SC_BAD_REQUEST, ErrorMessages.BAD_REQUEST, ErrorMessages.INVALID_PREFERENCE_CREATE, requestHandler.getInvalidCause());
        }

        String initPoint = preferencesService.save(requestHandler.getPreferenceDTO())
                .getInitPoint();
        response.status(HttpStatus.SC_OK);

        return initPoint;
    }

    public Object processPayment(Request request, Response response) {
        System.out.println("processPayment()");
        System.out.println(request.body());

        Payment payment = new Payment();
        try {
            payment.setToken(RequestUtil.getBodyParameter(request, "token"))
                    .setDescription("Algo muy codiciado")
                    .setInstallments(Integer.parseInt(RequestUtil.getBodyParameter(request, "installments")))
                    .setPaymentMethodId(RequestUtil.getBodyParameter(request, "payment_method"))
                    .setIssuerId(RequestUtil.getBodyParameter(request, "issuer_id"));

            // Si no llega como parametro (punto 4) deberia buscar la preference??
            // por ahora lo hago a pata...
            if(RequestUtil.getBodyParameter(request, "amount") == null) {
                Double transactionAmount = PreferenceModel.preference.getItems().stream()
                        .mapToDouble(i -> (i.getQuantity() * i.getUnitPrice()))
                        .sum();

                payment.setTransactionAmount(transactionAmount.floatValue());
            } else {
                payment.setTransactionAmount(Float.parseFloat(RequestUtil.getBodyParameter(request, "amount")));
            }
            com.mercadopago.resources.datastructures.payment.Payer payer = new com.mercadopago.resources.datastructures.payment.Payer();
            if(RequestUtil.getBodyParameter(request, "email") == null) {
                payer.setEmail(PreferenceModel.preference.getPayer().getEmail());
            } else {
                payer.setEmail(RequestUtil.getBodyParameter(request, "email"));
            }
            payment.setPayer(payer);

            payment.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Estado: " + payment.getStatus());

        return payment;
    }

    public Object finishPaymentProcess(Request request, Response response) {
        System.out.println("finishPaymentProcess()");
        System.out.println(request.body());

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

        return response;
    }

}
