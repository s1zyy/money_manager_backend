package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ExpenseRepositoryImpl implements ExpenseRepository {
    @Override
    public void save(Expense expense) {

    }

    @Override
    public void delete(Expense expense) {

    }

    @Override
    public Optional<Expense> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Expense> finaAll() {
        return List.of();
    }

    @Override
    public List<Expense> findAllByTripId(UUID id) {
        return List.of();
    }
}
