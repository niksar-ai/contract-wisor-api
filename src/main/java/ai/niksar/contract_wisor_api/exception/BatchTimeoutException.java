package ai.niksar.contract_wisor_api.exception;

public class BatchTimeoutException extends RuntimeException {

    public BatchTimeoutException(String message) {
        super(message);
    }

    public BatchTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}