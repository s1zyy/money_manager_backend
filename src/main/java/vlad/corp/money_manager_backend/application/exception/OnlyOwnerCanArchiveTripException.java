package vlad.corp.money_manager_backend.application.exception;

import java.util.UUID;

public class OnlyOwnerCanArchiveTripException extends RuntimeException {
    public OnlyOwnerCanArchiveTripException(UUID participantId) {
        super("Participant with id " + participantId + " is not the owner of the trip and cannot archive it.");
    }
}
