package vlad.corp.money_manager_backend.application.exceptions;

public class InvalidParticipantException extends RuntimeException {
    public InvalidParticipantException(String message) {
        super(message);
    }
}
