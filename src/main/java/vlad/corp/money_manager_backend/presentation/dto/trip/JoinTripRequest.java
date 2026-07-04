package vlad.corp.money_manager_backend.presentation.dto.trip;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record JoinTripRequest(
        String joinCode,
        @NotNull @DecimalMin("0") BigDecimal budget
) {}
