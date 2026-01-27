package vlad.corp.money_manager_backend.presentation.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDto(
        UUID transactionId,
        UUID walletId,
        BigDecimal amount,
        String currencyCode,
        String category,
        int version,
        UUID updatedBy,
        LocalDateTime dateTime
){}
