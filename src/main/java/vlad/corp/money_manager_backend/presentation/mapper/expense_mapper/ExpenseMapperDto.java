package vlad.corp.money_manager_backend.presentation.mapper.expense_mapper;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.presentation.dto.expense.ExpenseDto;

@Component
public class ExpenseMapperDto {

    public ExpenseDto toDto(Expense expense) {
        return new ExpenseDto(
                expense.getId(),
                expense.getTripId(),
                expense.getPayerId(),
                expense.getAmount().amount(),
                expense.getDate(),
                expense.getDescription(),
                expense.getParticipantIds().stream().toList()
        );
    }
}
