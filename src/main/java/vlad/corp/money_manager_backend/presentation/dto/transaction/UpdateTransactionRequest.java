package vlad.corp.money_manager_backend.presentation.dto.transaction;

import java.math.BigDecimal;

public record UpdateTransactionRequest(
        BigDecimal amount,
        String category,
        int expectedVersion
) {
}
