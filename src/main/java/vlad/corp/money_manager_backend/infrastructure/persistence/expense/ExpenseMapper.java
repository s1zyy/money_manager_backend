package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.SplitMode;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.util.HashMap;

@Component
public class ExpenseMapper {

    public Expense toDomain(ExpenseEntity entity) {
        return new Expense(
                entity.getId(),
                entity.getTripId(),
                new Money(entity.getAmount()),
                entity.getPayerId(),
                SplitMode.valueOf(entity.getSplitMode()),
                new HashMap<>(entity.getParticipantShares()),
                entity.getDate(),
                entity.getDescription(),
                entity.isPrepaid()
        );
    }

    public ExpenseEntity toEntity(Expense domain) {
        return new ExpenseEntity(
                domain.getId(),
                domain.getTripId(),
                domain.getAmount().amount(),
                domain.getPayerId(),
                domain.getSplitMode().name(),
                new HashMap<>(domain.getParticipantShares()),
                domain.getDate(),
                domain.getDescription(),
                domain.isPrepaid()
        );
    }
}
