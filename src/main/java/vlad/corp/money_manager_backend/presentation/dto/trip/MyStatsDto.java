package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.math.BigDecimal;
import java.util.UUID;

public record MyStatsDto(
        UUID participantId,
        BigDecimal budget,
        BigDecimal dailyLimit,
        BigDecimal spentToday
) {}
