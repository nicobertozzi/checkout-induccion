package controllers;

import com.google.common.net.MediaType;
import com.mercadopago.exceptions.MPException;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import spark.Request;
import spark.Response;
import utils.JsonUtils;

import java.util.ArrayList;

public class ExceptionHandler {

    private static void buildResponse(Response response, ErrorResponse errorResponse) {
        response.header("Content-Type", MediaType.JSON_UTF_8.toString());
        //response.body(Json.INSTANCE.toJsonString(errorResponse));
        response.body(JsonUtils.toJson(errorResponse));
        response.status(errorResponse.getHttpStatusCode());
    }

    public static void exceptionHandler(Exception exception, Request request, Response response) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, ErrorMessages.INTERNAL_ERROR, exception.getMessage(), new ArrayList<>());
        buildResponse(response, errorResponse);
    }

    public static void mpExceptionHandler(MPException mpException, Request request, Response response) {
        ErrorResponse errorResponse = new ErrorResponse(mpException.getStatusCode(), mpException.getCause().getMessage(), mpException.getMessage(), new ArrayList<>());
        buildResponse(response, errorResponse);
    }

}
