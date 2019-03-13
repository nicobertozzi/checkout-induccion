import com.mercadopago.exceptions.MPException;
import configuration.Configuration;
import configuration.Credentials;
import controller.EndpointController;
import controller.RenderScreenController;
import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import utils.JsonUtils;

public class RestEndpoint {

    public static void main(String[] args) throws MPException {
        Credentials.configureCredentials();
        Configuration.configureSpark();

        RestEndpoint.renderScreensDefinition();
        RestEndpoint.endpointsDefinition();

        System.out.println(">> Server is running...");
    }

    private static void renderScreensDefinition() {
        Spark.get("/", RenderScreenController::renderInicio, new ThymeleafTemplateEngine());
        Spark.get("/punto1", RenderScreenController::renderPunto1, new ThymeleafTemplateEngine());
        Spark.get("/punto2", RenderScreenController::renderPunto2, new ThymeleafTemplateEngine());
        Spark.get("/punto3", RenderScreenController::renderPunto3, new ThymeleafTemplateEngine());
        Spark.get("/punto4", RenderScreenController::renderPunto4, new ThymeleafTemplateEngine());
        Spark.get("/punto5", RenderScreenController::renderPunto5, new ThymeleafTemplateEngine());

        //Spark.get("/payment-status", RenderScreenController::renderPaymentStatus, new ThymeleafTemplateEngine());
    }

    private static void endpointsDefinition() {
        // Para el Punto 1 [Crear la preference]
        Spark.post("/create-preference", EndpointController::createPreference, JsonUtils.json());

        // Para el Punto 3 [Flujo de pago (v1)] y Punto 4 [Web Tokenize (v2)]
        Spark.post("/pay-preference", EndpointController::payPreference, JsonUtils.json());

        // Para el Punto 5 [Web Payment (v2)]
        Spark.post("/process-payment", EndpointController::processPayment, JsonUtils.json());
    }

}
