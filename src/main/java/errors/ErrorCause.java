package errors;

public class ErrorCause {

    private final String code;
    private final String description;

    public ErrorCause(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}