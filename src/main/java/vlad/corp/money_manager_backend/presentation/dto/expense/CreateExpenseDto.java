package vlad.corp.money_manager_backend.presentation.dto.expense;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record CreateExpenseDto(
        @NotNull @Positive BigDecimal amount,
        @NotNull UUID payerId,
        @NotNull LocalDate date,
        @NotEmpty Set<UUID> participantIds,
        String description
) {}
