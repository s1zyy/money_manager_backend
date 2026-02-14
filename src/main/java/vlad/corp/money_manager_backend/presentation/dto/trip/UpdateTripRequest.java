package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record UpdateTripRequest(
        String name,
        BigDecimal budget,
        BigDecimal prepaidExpenses,
        Set<UUID> participantIds,
        LocalDate startDate,
        LocalDate endDate
){}
