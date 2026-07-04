package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TripDto(
        UUID id,
        UUID ownerId,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        List<UUID> participantIds,
        Map<UUID, BigDecimal> participantBudgets,
        String currency,
        String joinCode,
        String status
) {}
