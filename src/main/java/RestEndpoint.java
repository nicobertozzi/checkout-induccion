import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class RestEndpoint {

    public static void main(String[] args) {
        Configuration.configureSpark();

        // Para el Punto 1 [Crear la preference] y Punto 2 [Obtener la preference y utilizar el SDK]
        Spark.get("/get-preference", (req, resp) -> Controller.getPreference(), JsonUtils.json());

        // Para el Punto 3 [Flujo de pago (v1)]
        Spark.post("/pay-preference", Controller::payPreference, JsonUtils.json());

        // Punto 4 [Web Tokenize (v2)]
        // Spark.post("/tokenize", RestEndpoint::tokenize, JsonUtils.json());

        // Punto 5 [Web Payment (v2)]
        // Spark.get("/payment-status", RestEndpoint::renderPaymentStatus, new ThymeleafTemplateEngine());
        // Spark.post("/process-payment", RestEndpoint::processPayment, JsonUtils.json());

        // Para el Punto 4 [Web Tokenize (v2)] y Punto 5 [Web Payment (v2)]
        Spark.get("/payment-status", Controller::renderPaymentStatus, new ThymeleafTemplateEngine());
        Spark.post("/tokenize_or_process-payment", Controller::tokenizeOrProcessPayment, JsonUtils.json());

        System.out.println(">> Server is running...");
    }

}
