package constants;

import com.google.common.net.MediaType;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;
import utils.JsonUtils;

import java.util.ArrayList;

public class Configuration {

    public static void configureSpark() {
        System.out.println(" >> Doing Spark's configuration...");

        // Cambiamos a un puerto mas amigable...
        Spark.port(9999);

        // Default Exception Handler
        Spark.exception(Exception.class, Configuration::exceptionHandler);

        System.out.println(" >> Spark's configuration done!");
    }

    private static void exceptionHandler(Exception exception, Request request, Response response) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, ErrorMessages.INTERNAL_ERROR, exception.getMessage(), new ArrayList<>());
        buildResponse(response, errorResponse);
    }

    private static void buildResponse(Response response, ErrorResponse errorResponse) {
        response.header("Content-Type", MediaType.JSON_UTF_8.toString());
        // String responseBody = Json.INSTANCE.toJsonString(errorResponse);
        String responseBody = JsonUtils.toJson(errorResponse);
        response.body(responseBody);
        response.status(errorResponse.getHttpStatusCode());
    }

}
