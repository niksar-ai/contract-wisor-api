package ai.niksar.contract_wisor_api.exception;

public class CriticalBatchException extends RuntimeException {
    public CriticalBatchException(String message) {
        super(message);
    }

    public CriticalBatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
