package router;

import controllers.EndpointController;
import controllers.RenderScreenController;
import controllers.handlers.RequestHandlerFactory;
import services.providers.ServicesProvider;
import spark.RouteGroup;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import utils.JsonTransformer;

public class RouteGroupImpl implements RouteGroup {

    @Override
    public void addRoutes() {
        /*
        Render's screens definition
         */
        Spark.get("/", RenderScreenController::renderInicio, new ThymeleafTemplateEngine());
        Spark.get("/punto1", RenderScreenController::renderPunto1, new ThymeleafTemplateEngine());
        Spark.get("/punto2", RenderScreenController::renderPunto2, new ThymeleafTemplateEngine());
        Spark.get("/punto3", RenderScreenController::renderPunto3, new ThymeleafTemplateEngine());
        Spark.get("/punto4", RenderScreenController::renderPunto4, new ThymeleafTemplateEngine());
        Spark.get("/punto5", RenderScreenController::renderPunto5, new ThymeleafTemplateEngine());
        //Spark.get("/payment-status", RenderScreenController::renderPaymentStatus, new ThymeleafTemplateEngine());

        /*
        Endpoints definition
         */
        RequestHandlerFactory requestHandlerFactory = new RequestHandlerFactory();
        EndpointController endpointController = new EndpointController(ServicesProvider.getPreferencesService(), requestHandlerFactory);

        // Para el Punto 1 [Crear la preference]
        Spark.post("/create-preference", endpointController::createPreference, new JsonTransformer());
        // Para el Punto 3 [Flujo de Pago (v1)] y Punto 4 [Web Tokenize (v2)]
        Spark.post("/process-payment", endpointController::processPayment, new JsonTransformer());
        // Para el Punto 5 [Web Payment (v2)]
        Spark.get("/finish-payment-process", endpointController::finishPaymentProcess, new JsonTransformer());
    }

}
