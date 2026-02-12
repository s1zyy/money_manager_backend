package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;


public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, UUID> {
    Set<ExpenseEntity> findAllByTripId(UUID id);
}
