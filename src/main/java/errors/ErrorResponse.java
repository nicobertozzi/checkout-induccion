package errors;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ErrorResponse {

    private final String message;
    private final String error;

    @JsonProperty("status")
    private final int httpStatusCode;
    @JsonProperty("cause")
    private final List<ErrorCause> errorCauses;

    public ErrorResponse(int httpStatusCode, String error, String message, List<ErrorCause> errorCauses) {
        this.httpStatusCode = httpStatusCode;
        this.error = error;
        this.message = message;
        this.errorCauses = errorCauses;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public List<ErrorCause> getErrorCauses() {
        return errorCauses;
    }

}