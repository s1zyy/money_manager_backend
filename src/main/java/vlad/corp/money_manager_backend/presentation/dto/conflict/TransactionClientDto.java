package vlad.corp.money_manager_backend.presentation.dto.conflict;

import java.math.BigDecimal;

public record TransactionClientDto(
        BigDecimal amount,
        String category,
        int expectedVersion
) {}
