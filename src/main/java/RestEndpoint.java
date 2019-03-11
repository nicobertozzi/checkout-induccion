import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import spark.*;
import com.mercadopago.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class RestEndpoint {

    // Checkout basico
    private static final String SELLER_CLIENT_ID = "2799634616083392";
    private static final String SELLER_CLIENT_SECRET = "ZWNgyLUjf3F1Z0lH3yawRNYvSD73p6OH";
    // Checkout personalizado
    private static final String SELLER_ACCESS_TOKEN = "TEST-2799634616083392-030613-37d6c9f41b71f2b5dc2170a70cf3d211-401515673";

    // PAYER
    private static final String PAYER_NICK = "TESTMKRSPLCN";
    private static final String PAYER_EMAIL = "test_user_19562067@testuser.com";

    // String para almacenar la ruta a donde ir dependiendo del estado del pago...
    static String view = null;

    public static void main(String[] args) {
        try {
            configureSpark();
            configureCredentials();

            System.out.println(">> Configuration done!");
        } catch (MPException mpe) {
            mpe.printStackTrace();
        }

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
    Configuracion inicial de Spark...
     */
    private static void configureSpark() {
        // Cambiamos a un puerto mas amigable...
        Spark.port(9999);

        // Al hacer el fetch desde React para obtener los datos, se bloqueaba el acceso por CORS policy (por eso se devuelve en la cabecera)
        Spark.after((Filter) (req, resp) -> {
            resp.header("Access-Control-Allow-Origin", "*");
            resp.header("Access-Control-Allow-Methods", "GET");
        });
    }
    /*
    Configuracion de las credenciales de un usuario de prueba test_user_84296691@testuser.com
     */
    private static void configureCredentials() throws MPException {
        MercadoPago.SDK.setClientId(SELLER_CLIENT_ID);
        MercadoPago.SDK.setClientSecret(SELLER_CLIENT_SECRET);
    }

    /*
    Configuracion para el Access Token...
     */
    public static void configureAccessToken() throws MPException {
        MercadoPago.SDK.setAccessToken(SELLER_ACCESS_TOKEN);
    }

    /*
    Creamos una preference a partir de un Payer e Items...
     */
    private static Preference getPreference() {
        Preference locPreference = new Preference();
        try {
            // Creamos un Payer de Test...
            locPreference.setPayer(new Payer()
                    .setName("TEST")
                    .setSurname(PAYER_NICK)
                    .setEmail(PAYER_EMAIL)
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
            configureAccessToken();

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
            configureAccessToken();

            payment.setTransactionAmount(300F)
                    .setToken(request.queryParams("token"))
                    .setDescription("Algo muy codiciado 2")
                    .setInstallments(Integer.valueOf(request.queryParams("installments")))
                    .setPaymentMethodId(request.queryParams("payment_method_id"))
                    .setIssuerId(request.queryParams("issuer_id"))
                    .setPayer(new com.mercadopago.resources.datastructures.payment.Payer()
                            .setEmail(PAYER_EMAIL));

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
