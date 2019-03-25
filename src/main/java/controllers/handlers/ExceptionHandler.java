package controllers.handlers;

import com.google.common.net.MediaType;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.exceptions.MPValidationException;
import errors.ErrorCause;
import errors.ErrorMessages;
import errors.ErrorResponse;
import org.apache.http.HttpStatus;
import spark.Request;
import spark.Response;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class ExceptionHandler {

    private static void buildResponse(Response response, ErrorResponse errorResponse) {
        response.header("Content-Type", MediaType.JSON_UTF_8.toString());
        response.body(JsonUtils.toJson(errorResponse));
        response.status(errorResponse.getHttpStatusCode());
    }

    public static void exceptionHandler(Exception exception, Request request, Response response) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, ErrorMessages.INTERNAL_ERROR, exception.getMessage(), new ArrayList<>());
        buildResponse(response, errorResponse);
    }

    public static void mpExceptionHandler(MPException mpException, Request request, Response response) {
        int status = mpException.getStatusCode() != null ? mpException.getStatusCode() : HttpStatus.SC_INTERNAL_SERVER_ERROR;
        String error = (mpException.getCause() != null && mpException.getCause().getMessage() != null) ? mpException.getCause().getMessage() : ErrorMessages.INTERNAL_ERROR;
        String message = mpException.getMessage() != null ? mpException.getMessage() : "";
        List<ErrorCause> causes = (mpException instanceof MPValidationException) ? new ArrayList(((MPValidationException) mpException).getColViolations()) : new ArrayList<>();

        ErrorResponse errorResponse = new ErrorResponse(status, error, message, causes);
        buildResponse(response, errorResponse);
    }

}
