package vlad.corp.money_manager_backend.presentation.dto.trip;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTripRequest(
        @NotBlank String name,
        @NotNull @DecimalMin("0") BigDecimal totalBudget,
        @NotNull @DecimalMin("0") BigDecimal prepaidExpenses,
        LocalDate startDate,
        LocalDate endDate,
        String currency
) {}
