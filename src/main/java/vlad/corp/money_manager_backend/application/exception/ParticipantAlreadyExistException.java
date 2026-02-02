package vlad.corp.money_manager_backend.application.exception;

public class ParticipantAlreadyExistException extends RuntimeException {
    public ParticipantAlreadyExistException(String message) {
        super(message);
    }
}
