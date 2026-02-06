package vlad.corp.money_manager_backend.presentation.dto.trip;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTripRequest(
        String name,
        BigDecimal totalBudget,
        BigDecimal prepaidExpenses,
        LocalDate startDate,
        LocalDate endDate
) {}
