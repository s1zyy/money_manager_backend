package vlad.corp.money_manager_backend.presentation.dto.expense;

import vlad.corp.money_manager_backend.domain.model.SplitMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record UpdateExpenseDto(
        BigDecimal amount,
        LocalDate date,
        SplitMode splitMode,
        Set<UUID> newParticipantIds,
        Map<UUID, BigDecimal> customShares,
        String description
) {}
