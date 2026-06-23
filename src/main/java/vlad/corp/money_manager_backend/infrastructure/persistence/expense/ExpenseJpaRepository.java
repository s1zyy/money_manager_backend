package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Set;
import java.util.UUID;


public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, UUID> {
    Set<ExpenseEntity> findAllByTripId(UUID id);
    @Query("SELECT DISTINCT p FROM ExpenseEntity e JOIN e.participantIds p WHERE e.tripId = :tripId")
    Set<UUID> findDistinctParticipantsInExpenses(@Param("tripId") UUID tripId);
    @Query("SELECT DISTINCT e.payerId FROM ExpenseEntity e WHERE e.tripId = :tripId")
    Set<UUID> findDistinctPayersInExpenses(@Param("tripId") UUID tripId);
    @Modifying
    @Query(value = "DELETE FROM expenses WHERE trip_id = :tripId", nativeQuery = true)
    void deleteAllByTripId(@Param("tripId") UUID tripId);
}
