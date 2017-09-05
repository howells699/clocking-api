package ClockingAPI.exceptions;

public class APIException extends Throwable {

    private int code;

    public APIException() {
        this(500);
    }
    public APIException(int code) {
        this(code, "Error while processing the request", null);
    }
    public APIException(int code, String message) {
        this(code, message, null);
    }
    public APIException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}