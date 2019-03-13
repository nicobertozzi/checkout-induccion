package controller;

import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import configuration.Credentials;
import model.Model;
import spark.Request;
import spark.Response;
import utils.RequestUtils;

public class EndpointController {

    /*
    Creamos una Preference a partir de un Payer e Items...
     */
    public static Preference createPreference(Request request, Response response) {
        System.out.println("createPreference()");
        System.out.println(request.body());

        Preference locPreference = new Preference();
        try {
            // Creamos un Payer de Test...
            locPreference.setPayer(new Payer()
                    .setName(RequestUtils.getBodyParameter(request, "payerName"))
                    .setSurname(RequestUtils.getBodyParameter(request, "payerSurname"))
                    .setEmail(RequestUtils.getBodyParameter(request, "payerEmail"))
                    .setIdentification(new Identification()
                            .setType(RequestUtils.getBodyParameter(request, "payerTypeDNI"))
                            .setNumber(RequestUtils.getBodyParameter(request, "payerNumberDNI"))));

            // Creamos un item para la compra y lo añadimos...
            locPreference.appendItem(new Item()
                    .setTitle(RequestUtils.getBodyParameter(request, "itemTitle"))
                    .setQuantity(Integer.parseInt(RequestUtils.getBodyParameter(request, "itemQuantity")))
                    .setCurrencyId("ARS")
                    .setUnitPrice(Float.parseFloat(RequestUtils.getBodyParameter(request, "itemUnitPrice"))));

            locPreference.save();

            // Si esta bien, recien ahi reemplazamos...
            Model.preference = locPreference;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return locPreference;
    }

    public static Object payPreference(Request request, Response response) {
        System.out.println("payPreference()");
        System.out.println(request.body());

        Payment payment = new Payment();
        try {
            Credentials.configureAccessToken();

            payment.setTransactionAmount(Float.parseFloat(RequestUtils.getBodyParameter(request, "amount")))
                    .setToken(RequestUtils.getBodyParameter(request, "token"))
                    .setDescription("Algo muy codiciado")
                    .setInstallments(Integer.parseInt(RequestUtils.getBodyParameter(request, "installments")))
                    .setPaymentMethodId(RequestUtils.getBodyParameter(request, "payment_method"))
                    .setIssuerId(RequestUtils.getBodyParameter(request, "issuer_id"))
                    .setPayer(new com.mercadopago.resources.datastructures.payment.Payer()
                            .setEmail(RequestUtils.getBodyParameter(request, "email")));

            payment.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Estado: " + payment.getStatus());

        return payment;
    }

    public static Object processPayment(Request request, Response response) {
        System.out.println("processPayment()");
        System.out.println(request.body());

        //RequestUtils.getBodyParameter(request, "payment_status");

        System.out.println("payment status = " + request.queryParams("payment_status"));
        System.out.println("preference id = " + request.queryParams("preference_id"));
        System.out.println("back url = " + request.queryParams("back_url"));
        System.out.println("merchant order id = " + request.queryParams("merchant_order_id"));
        System.out.println("payment status detail = " + request.queryParams("payment_status_detail"));
        System.out.println("payment id = " + request.queryParams("payment_id"));

        return response;
    }

}
