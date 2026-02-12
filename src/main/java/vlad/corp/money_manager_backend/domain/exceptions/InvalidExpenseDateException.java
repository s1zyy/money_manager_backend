package vlad.corp.money_manager_backend.domain.exceptions;

public class InvalidExpenseDateException extends RuntimeException {
    public InvalidExpenseDateException(String message) {
        super(message);
    }
}
