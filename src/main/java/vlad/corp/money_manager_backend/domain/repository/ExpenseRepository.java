package vlad.corp.money_manager_backend.domain.repository;

import vlad.corp.money_manager_backend.domain.model.Expense;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ExpenseRepository {
    void save(Expense expense);
    void delete(Expense expense);
    Optional<Expense> findById(UUID id);
    List<Expense> findAll();
    List<Expense> findAllByTripId(UUID id);
    Set<UUID> findActiveParticipantIds(UUID tripId);
}
