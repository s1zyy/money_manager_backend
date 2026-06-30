package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.math.BigDecimal;
import java.util.UUID;

public record SettlementTransferDto(
        UUID fromId,
        String fromName,
        UUID toId,
        String toName,
        BigDecimal amount
) {}
