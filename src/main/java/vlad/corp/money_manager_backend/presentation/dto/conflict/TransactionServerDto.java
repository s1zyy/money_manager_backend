package vlad.corp.money_manager_backend.presentation.dto.conflict;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionServerDto(
        UUID transactionId,
        UUID walletId,
        BigDecimal amount,
        String category,
        int version,
        UUID updatedBy,
        Instant updatedAt
) {}
