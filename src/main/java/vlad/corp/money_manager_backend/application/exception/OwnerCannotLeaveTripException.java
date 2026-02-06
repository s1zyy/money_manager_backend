package vlad.corp.money_manager_backend.application.exception;

public class OwnerCannotLeaveTripException extends RuntimeException {
    public OwnerCannotLeaveTripException(String message) {
        super(message);
    }
}
