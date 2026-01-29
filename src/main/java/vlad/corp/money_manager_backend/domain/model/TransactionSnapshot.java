package vlad.corp.money_manager_backend.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionSnapshot (
        UUID transactionId,
        UUID walletId,
        UUID updatedBy,
        int version,
        BigDecimal amount,
        String currencyCode,
        String category,
        Instant updatedAt
    ){}
