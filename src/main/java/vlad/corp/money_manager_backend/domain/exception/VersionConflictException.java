package vlad.corp.money_manager_backend.domain.exception;

import lombok.Getter;
import vlad.corp.money_manager_backend.domain.model.ClientAttemptSnapshot;
import vlad.corp.money_manager_backend.domain.model.TransactionSnapshot;

import java.util.UUID;

@Getter
public class VersionConflictException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private final UUID transactionId;
    private final int currentVersion;
    private final int expectedVersion;

    private TransactionSnapshot transactionSnapshot;
    private ClientAttemptSnapshot clientAttemptSnapshot;

    public VersionConflictException(UUID transactionId, int currentVersion, int expectedVersion) {
        super(String.format(
                "Version conflict for transaction %s: current version %d, expected version %d",
                transactionId, currentVersion, expectedVersion
        ));
        this.transactionId = transactionId;
        this.currentVersion = currentVersion;
        this.expectedVersion = expectedVersion;
    }

    public VersionConflictException(UUID transactionId, int currentVersion, int expectedVersion, String message) {
        super(message);
        this.transactionId = transactionId;
        this.currentVersion = currentVersion;
        this.expectedVersion = expectedVersion;
    }

    public void attachTransactionSnapshot(TransactionSnapshot transactionSnapshot) {
        this.transactionSnapshot = transactionSnapshot;
    }
    public void attachClientAttemptSnapshot(ClientAttemptSnapshot clientAttemptSnapshot) {
        this.clientAttemptSnapshot = clientAttemptSnapshot;
    }
}
