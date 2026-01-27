package vlad.corp.money_manager_backend.presentation.dto.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequest(
        UUID walletId,
        BigDecimal amount,
        String category
) {
}
