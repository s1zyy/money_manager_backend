package vlad.corp.money_manager_backend.domain.exceptions;

public class OwnerCannotLeaveTripException extends RuntimeException {
    public OwnerCannotLeaveTripException(String message) {
        super(message);
    }
}
