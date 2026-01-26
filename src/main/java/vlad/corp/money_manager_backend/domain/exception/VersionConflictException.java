package vlad.corp.money_manager_backend.domain.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class VersionConflictException extends RuntimeException{

    private final UUID transactionId;
    private final int currentVersion;
    private final int expectedVersion;

    public VersionConflictException(UUID transactionId, int currentVersion, int expectedVersion) {
        super("Version conflict");
        this.transactionId = transactionId;
        this.currentVersion = currentVersion;
        this.expectedVersion = expectedVersion;
    }
}
