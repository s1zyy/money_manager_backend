package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.util.HashSet;

@Component
public class ExpenseMapper {

    public Expense toDomain(ExpenseEntity entity) {
        return new Expense(
                entity.getId(),
                entity.getTripId(),
                new Money(entity.getAmount()),
                entity.getPayerId(),
                entity.getParticipantIds().stream().toList(),
                entity.getDate(),
                entity.getDescription()
        );
    }

    public ExpenseEntity toEntity(Expense domain) {
        return new ExpenseEntity(
                domain.getId(),
                domain.getTripId(),
                domain.getAmount().getAmount(),
                domain.getPayerId(),
                new HashSet<>(domain.getParticipantIds()),
                domain.getDate(),
                domain.getDescription()
        );
    }
}
