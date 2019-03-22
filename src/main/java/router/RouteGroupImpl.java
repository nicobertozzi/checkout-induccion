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
        Spark.get("/", RenderScreenController::renderIndex, new ThymeleafTemplateEngine());
        Spark.get("/page1", RenderScreenController::renderPage1, new ThymeleafTemplateEngine());
        Spark.get("/page2", RenderScreenController::renderPage2, new ThymeleafTemplateEngine());
        Spark.get("/page3", RenderScreenController::renderPage3, new ThymeleafTemplateEngine());
        Spark.get("/page4", RenderScreenController::renderPage4, new ThymeleafTemplateEngine());
        Spark.get("/page5", RenderScreenController::renderPage5, new ThymeleafTemplateEngine());

        /*
        Endpoints definition
         */
        RequestHandlerFactory requestHandlerFactory = new RequestHandlerFactory();
        EndpointController endpointController = new EndpointController(ServicesProvider.getPreferencesService(), ServicesProvider.getPaymentsService(), requestHandlerFactory);

        // Para el Punto 1 [Crear la preference]
        Spark.post("/create-preference", endpointController::createPreference, new JsonTransformer());
        // Para el Punto 3 [Flujo de Pago (v1)] y Punto 4 [Web Tokenize (v2)]
        Spark.post("/process-payment", endpointController::processPayment, new JsonTransformer());
        // Para el Punto 5 [Web Payment (v2)]
        Spark.get("/finish-payment-process", endpointController::finishPaymentProcess, new JsonTransformer());
    }

}
