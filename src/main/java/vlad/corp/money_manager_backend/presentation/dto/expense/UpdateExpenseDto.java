package vlad.corp.money_manager_backend.presentation.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record UpdateExpenseDto(
        BigDecimal amount,
        LocalDate date,
        Set<UUID> newParticipantIds,
        String description
) {
}
