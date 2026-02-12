package vlad.corp.money_manager_backend.domain.exceptions;

public class ParticipantAlreadyExistException extends RuntimeException {
    public ParticipantAlreadyExistException(String message) {
        super(message);
    }
}
