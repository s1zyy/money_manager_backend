package vlad.corp.money_manager_backend.domain.model;

import java.math.BigDecimal;

public record ClientAttemptSnapshot(
        BigDecimal amount,
        String category,
        int expectedVersion
) {
}
