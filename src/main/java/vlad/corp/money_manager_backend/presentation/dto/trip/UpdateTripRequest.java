package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record UpdateTripRequest(
        String name,
        BigDecimal budget,
        BigDecimal prepaidExpenses,
        List<UUID> participantIds
){}
