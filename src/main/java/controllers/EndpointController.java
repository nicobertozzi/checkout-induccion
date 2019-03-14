package controllers;

import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import model.PreferenceModel;
import spark.Request;
import spark.Response;
import utils.RequestUtil;

public class EndpointController {

    /*
    Creamos una Preference a partir de un Payer e Items...
     */
    public static Preference createPreference(Request request, Response response) {
        System.out.println("createPreference()");
        System.out.println(request.body());

        Preference preference = new Preference();
        try {
            // Creamos un Payer de Test...
            preference.setPayer(new Payer()
                    .setName(RequestUtil.getBodyParameter(request, "payerName"))
                    .setSurname(RequestUtil.getBodyParameter(request, "payerSurname"))
                    .setEmail(RequestUtil.getBodyParameter(request, "payerEmail"))
                    .setIdentification(new Identification()
                            .setType(RequestUtil.getBodyParameter(request, "payerTypeDNI"))
                            .setNumber(RequestUtil.getBodyParameter(request, "payerNumberDNI"))));

            // Creamos un item para la compra y lo añadimos...
            preference.appendItem(new Item()
                    .setTitle(RequestUtil.getBodyParameter(request, "itemTitle"))
                    .setQuantity(Integer.parseInt(RequestUtil.getBodyParameter(request, "itemQuantity")))
                    .setCurrencyId("ARS")
                    .setUnitPrice(Float.parseFloat(RequestUtil.getBodyParameter(request, "itemUnitPrice"))));

            preference.save();

            // Si esta bien, recien ahi reemplazamos...
            PreferenceModel.preference = preference;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return preference;
    }

    public static Object payPreference(Request request, Response response) {
        System.out.println("payPreference()");
        System.out.println(request.body());

        Payment payment = new Payment();
        try {
            payment.setTransactionAmount(Float.parseFloat(RequestUtil.getBodyParameter(request, "amount")))
                    .setToken(RequestUtil.getBodyParameter(request, "token"))
                    .setDescription("Algo muy codiciado")
                    .setInstallments(Integer.parseInt(RequestUtil.getBodyParameter(request, "installments")))
                    .setPaymentMethodId(RequestUtil.getBodyParameter(request, "payment_method"))
                    .setIssuerId(RequestUtil.getBodyParameter(request, "issuer_id"))
                    .setPayer(new com.mercadopago.resources.datastructures.payment.Payer()
                            .setEmail(RequestUtil.getBodyParameter(request, "email")));

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

        //RequestUtil.getBodyParameter(request, "payment_status");

        System.out.println("payment status = " + request.queryParams("payment_status"));
        System.out.println("preference id = " + request.queryParams("preference_id"));
        System.out.println("back url = " + request.queryParams("back_url"));
        System.out.println("merchant order id = " + request.queryParams("merchant_order_id"));
        System.out.println("payment status detail = " + request.queryParams("payment_status_detail"));
        System.out.println("payment id = " + request.queryParams("payment_id"));

        return response;
    }

}
