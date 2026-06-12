package vlad.corp.money_manager_backend.presentation.dto.expense;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExpenseDto(
        UUID id,
        UUID tripId,
        UUID payerId,
        BigDecimal amount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,
        String description,
        List<UUID> participantIds
) {
}
