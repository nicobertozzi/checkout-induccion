package routes;

import com.google.common.net.MediaType;
import com.mercadopago.exceptions.MPException;
import constants.Configuration;
import constants.Credentials;
import controllers.EndpointController;
import controllers.RenderScreenController;
import utils.Json;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import spark.*;
import spark.servlet.SparkApplication;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import utils.JsonTransformer;

import java.util.ArrayList;

public class RestEndpoint implements SparkApplication {

    @Override
    public void init() {
        try {
            Configuration.configureSpark();
            Credentials.configureCredentials();

            renderScreensDefinition();
            endpointsDefinition();
        } catch (MPException e) {
            e.printStackTrace();
        }

        // Default Exception Handler
        Spark.exception(Exception.class, RestEndpoint::exceptionHandler);
    }

    private void renderScreensDefinition() {
        Spark.get("/", RenderScreenController::renderInicio, new ThymeleafTemplateEngine());
        Spark.get("/punto1", RenderScreenController::renderPunto1, new ThymeleafTemplateEngine());
        Spark.get("/punto2", RenderScreenController::renderPunto2, new ThymeleafTemplateEngine());
        Spark.get("/punto3", RenderScreenController::renderPunto3, new ThymeleafTemplateEngine());
        Spark.get("/punto4", RenderScreenController::renderPunto4, new ThymeleafTemplateEngine());
        Spark.get("/punto5", RenderScreenController::renderPunto5, new ThymeleafTemplateEngine());

        //Spark.get("/payment-status", RenderScreenController::renderPaymentStatus, new ThymeleafTemplateEngine());
    }

    private void endpointsDefinition() {
        // Para el Punto 1 [Crear la preference]
        Spark.post("/create-preference", EndpointController::createPreference, new JsonTransformer());

        // Para el Punto 3 [Flujo de pago (v1)] y Punto 4 [Web Tokenize (v2)]
        Spark.post("/pay-preference", EndpointController::payPreference, new JsonTransformer());

        // Para el Punto 5 [Web Payment (v2)]
        Spark.post("/process-payment", EndpointController::processPayment, new JsonTransformer());
    }

    private static void exceptionHandler(Exception exception, Request request, Response response) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, ErrorMessages.INTERNAL_ERROR, exception.getMessage(), new ArrayList<>());
        buildResponse(response, errorResponse);
    }

    private static void buildResponse(Response response, ErrorResponse errorResponse) {
        response.header("Content-Type", MediaType.JSON_UTF_8.toString());
        String responseBody = Json.INSTANCE.toJsonString(errorResponse);
        response.body(responseBody);
        response.status(errorResponse.getHttpStatusCode());
    }

}
