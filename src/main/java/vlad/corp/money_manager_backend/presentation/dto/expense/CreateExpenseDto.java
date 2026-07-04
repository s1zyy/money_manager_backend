package vlad.corp.money_manager_backend.presentation.dto.expense;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import vlad.corp.money_manager_backend.domain.model.SplitMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record CreateExpenseDto(
        @NotNull @Positive BigDecimal amount,
        UUID payerId,
        LocalDate date,
        @NotNull SplitMode splitMode,
        Set<UUID> participantIds,
        Map<UUID, BigDecimal> customShares,
        String description,
        boolean isPrepaid
) {}
