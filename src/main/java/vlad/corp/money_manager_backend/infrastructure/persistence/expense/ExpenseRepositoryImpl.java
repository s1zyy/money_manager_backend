package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import java.util.*;

@Repository
public class ExpenseRepositoryImpl implements ExpenseRepository {
    private final ExpenseJpaRepository jpaRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseRepositoryImpl(ExpenseJpaRepository jpaRepository, ExpenseMapper expenseMapper) {
        this.jpaRepository = jpaRepository;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public void save(Expense expense) {
        jpaRepository.save(expenseMapper.toEntity(expense));
    }

    @Override
    public void delete(Expense expense) {
        jpaRepository.delete(expenseMapper.toEntity(expense));
    }

    @Override
    public Optional<Expense> findById(UUID id) {
        return jpaRepository.findById(id).map(expenseMapper::toDomain);
    }

    @Override
    public List<Expense> findAllByTripId(UUID id) {
        return jpaRepository.findAllByTripId(id)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public Set<UUID> findActiveParticipantIds(UUID tripId) {
        Set<UUID> ids = new HashSet<>(jpaRepository.findDistinctParticipantsInExpenses(tripId));
        ids.addAll(jpaRepository.findDistinctPayersInExpenses(tripId));
        return ids;
    }

    @Override
    @Transactional
    public void deleteAllByTripId(UUID tripId) {
        jpaRepository.deleteAllByTripId(tripId);
    }

    @Override
    @Transactional
    public void reassignParticipant(UUID tripId, UUID fromParticipantId, UUID toParticipantId) {
        jpaRepository.reassignPayer(tripId, fromParticipantId, toParticipantId);
        jpaRepository.reassignParticipantShares(tripId, fromParticipantId, toParticipantId);
    }

    @Override
    public boolean hasAnyExpenseInvolvement(UUID participantId) {
        return jpaRepository.existsAnyExpenseInvolvement(participantId);
    }
}
