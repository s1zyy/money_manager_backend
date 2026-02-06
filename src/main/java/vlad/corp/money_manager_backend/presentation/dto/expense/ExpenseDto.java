package vlad.corp.money_manager_backend.presentation.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExpenseDto(
        UUID id,
        UUID payerId,
        BigDecimal amount,
        LocalDate date,
        String description,
        List<UUID> participantIds
) {}
