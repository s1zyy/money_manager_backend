package vlad.corp.money_manager_backend.presentation.dto.conflict;

import java.util.UUID;

public record VersionConflictResponse(
        UUID transactionId,
        int currentVersion,
        int expectedVersion,
        TransactionServerDto serverTransaction,
        TransactionClientDto clientTransaction,
        String message
) {}
