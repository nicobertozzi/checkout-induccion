import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;

public class RestEndpoint {

    // String para almacenar la ruta a donde ir dependiendo del estado del pago...
    private static String view = null;

    public static void main(String[] args) {
        Configuration.configureSpark();

        // Para el Punto 1 [Crear la preference] y Punto 2 [Obtener la preference y utilizar el SDK]
        Spark.get("/get-preference", (req, resp) -> getPreference(), JsonUtils.json());

        // Para el Punto 3 [Flujo de pago (v1)]
        Spark.post("/pay-preference", RestEndpoint::payPreference, JsonUtils.json());

        // Punto 4 [Web Tokenize (v2)]
        // Spark.post("/tokenize", RestEndpoint::tokenize, JsonUtils.json());

        // Punto 5 [Web Payment (v2)]
        // Spark.get("/payment-status", RestEndpoint::renderPaymentStatus, new ThymeleafTemplateEngine());
        // Spark.post("/process-payment", RestEndpoint::processPayment, JsonUtils.json());

        // Para el Punto 4 [Web Tokenize (v2)] y Punto 5 [Web Payment (v2)]
        Spark.get("/payment-status", RestEndpoint::renderPaymentStatus, new ThymeleafTemplateEngine());
        Spark.post("/tokenize_or_process-payment", RestEndpoint::tokenizeOrProcessPayment, JsonUtils.json());

        System.out.println(">> Server is running...");
    }

    /*
    Creamos una Preference a partir de un Payer e Items...
     */
    private static Preference getPreference() {
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
                            .setNumber("20202020")));

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

    private static Object payPreference(Request request, Response response) {
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
        // Aca entra al punto 4 porque manda el token al tokanizar...
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
        // Aca entra al punto 5 porque manda el estado del pago...
        System.out.println("processPayment()");

        System.out.println("payment status = " + request.queryParams("payment_status"));
        System.out.println("preference id = " + request.queryParams("preference_id"));
        System.out.println("back url = " + request.queryParams("back_url"));
        System.out.println("merchant order id = " + request.queryParams("merchant_order_id"));
        System.out.println("payment status detail = " + request.queryParams("payment_status_detail"));
        System.out.println("payment id = " + request.queryParams("payment_id"));

        view = request.queryParams("payment_status");
        response.redirect("payment-status");

        return response;
    }

    /*
    Renderizado de la vista dependiendo del estado del pago realizado en el punto 5
     */
    private static ModelAndView renderPaymentStatus(Request request, Response response) {
        return new ModelAndView(new HashMap<>(), view);
    }

    /*
    Tuve que mantener los 2 metodos en uno solo porque los script de los botones (del punto 4 y 5) se pisaban entre ellos los endpoint...
    Entiendo que no podian convivir en la misma pantalla...
     */
    private static Object tokenizeOrProcessPayment(Request request, Response response) {
        if(request.queryParams("token") != null) {
            return tokenize(request, response);
        } else if(request.queryParams("payment_status") != null) {
            return processPayment(request, response);
        }

        System.out.println("Ninguno...");

        return null;
    }

}
