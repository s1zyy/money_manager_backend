package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateTripRequest(
        String name,
        BigDecimal budget,
        BigDecimal prepaidExpenses,
        LocalDate endDate,
        String currency
){}
