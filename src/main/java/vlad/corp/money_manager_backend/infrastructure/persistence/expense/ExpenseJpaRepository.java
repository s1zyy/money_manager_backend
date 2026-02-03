package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, UUID> {
    List<ExpenseEntity> findAllByTripId(UUID id);
}
