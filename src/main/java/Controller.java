import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;

class Controller {

    // String para almacenar la ruta a donde ir dependiendo del estado del pago...
    private static String view = null;

    /*
    Creamos una Preference a partir de un Payer e Items...
     */
    static Preference getPreference() {
        System.out.println("getPreference()");

        Preference locPreference = new Preference();
        try {
            // Creamos un Payer de Test...
            locPreference.setPayer(new Payer()
                    .setName("TEST")
                    .setSurname(Credentials.PAYER_NICK)
                    .setEmail(Credentials.PAYER_EMAIL)
                    .setIdentification(new Identification()
                            .setType("DNI")
                            .setNumber("13631413")));

            // Creamos un item para la compra y lo añadimos...
            locPreference.appendItem(new Item()
                    .setTitle("Item-super-generico-1")
                    .setQuantity(2)
                    .setCurrencyId("ARS")
                    .setUnitPrice(2100F));
            locPreference.appendItem(new Item()
                    .setTitle("Item-super-generico-2")
                    .setQuantity(5)
                    .setCurrencyId("ARS")
                    .setUnitPrice(350F));

            locPreference.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return locPreference;
    }

    static Object payPreference(Request request, Response response) {
        System.out.println("payPreference()");

        Payment payment = new Payment();
        try {
            Credentials.configureAccessToken();

            payment.setTransactionAmount(Float.parseFloat(request.queryParams("amount")))
                    .setToken(request.queryParams("token"))
                    .setDescription("Algo muy codiciado")
                    .setInstallments(Integer.parseInt(request.queryParams("installments")))
                    .setPaymentMethodId(request.queryParams("paymentMethod"))
                    .setPayer(new com.mercadopago.resources.datastructures.payment.Payer()
                            .setEmail(request.queryParams("email")));

            payment.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Estado: " + payment.getStatus());

        return payment;
    }

    private static Object tokenize(Request request, Response response) {
        System.out.println("tokenize()");

        Payment payment = new Payment();
        try {
            Credentials.configureAccessToken();

            payment.setTransactionAmount(300F)
                    .setToken(request.queryParams("token"))
                    .setDescription("Algo muy codiciado 2")
                    .setInstallments(Integer.valueOf(request.queryParams("installments")))
                    .setPaymentMethodId(request.queryParams("payment_method_id"))
                    .setIssuerId(request.queryParams("issuer_id"))
                    .setPayer(new com.mercadopago.resources.datastructures.payment.Payer()
                            .setEmail(Credentials.PAYER_EMAIL));

            payment.save();
        } catch (Exception e) {
            payment.setStatus(Payment.Status.rejected);
            e.printStackTrace();
        }

        System.out.println("Estado: " + payment.getStatus());

        return payment;
    }

    private static Object processPayment(Request request, Response response) {
        System.out.println("processPayment()");

        System.out.println("payment status = " + request.queryParams("payment_status"));
        System.out.println("preference id = " + request.queryParams("preference_id"));
        System.out.println("back url = " + request.queryParams("back_url"));
        System.out.println("merchant order id = " + request.queryParams("merchant_order_id"));
        System.out.println("payment status detail = " + request.queryParams("payment_status_detail"));
        System.out.println("payment id = " + request.queryParams("payment_id"));

        // la idea aca es usar las back_url...
        view = request.queryParams("payment_status");
        response.redirect("payment-status");

        return response;
    }

    /*
    Renderizado de la vista dependiendo del estado del pago realizado en el punto 5
     */
    static ModelAndView renderPaymentStatus(Request request, Response response) {
        return new ModelAndView(new HashMap<>(), view);
    }

    /*
    Tuve que mantener los 2 metodos en uno solo porque los script de los botones (del punto 4 y 5) se pisaban entre ellos los endpoint...
    Entiendo que no podian convivir en la misma pantalla...
     */
    static Object tokenizeOrProcessPayment(Request request, Response response) {
        if(request.queryParams("token") != null) {
            return tokenize(request, response);
        } else if(request.queryParams("payment_status") != null) {
            return processPayment(request, response);
        }

        System.out.println("Ninguno...");

        return null;
    }

}
