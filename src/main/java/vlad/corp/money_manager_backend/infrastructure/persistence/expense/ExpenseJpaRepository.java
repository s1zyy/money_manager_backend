package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Set;
import java.util.UUID;


public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, UUID> {
    Set<ExpenseEntity> findAllByTripId(UUID id);
    @Query("SELECT DISTINCT KEY(ps) FROM ExpenseEntity e JOIN e.participantShares ps WHERE e.tripId = :tripId")
    Set<UUID> findDistinctParticipantsInExpenses(@Param("tripId") UUID tripId);
    @Query("SELECT DISTINCT e.payerId FROM ExpenseEntity e WHERE e.tripId = :tripId")
    Set<UUID> findDistinctPayersInExpenses(@Param("tripId") UUID tripId);
    @Modifying
    @Query(value = "DELETE FROM expenses WHERE trip_id = :tripId", nativeQuery = true)
    void deleteAllByTripId(@Param("tripId") UUID tripId);

    @Modifying
    @Query(value = "UPDATE expenses SET payer_id = :toId WHERE payer_id = :fromId AND trip_id = :tripId", nativeQuery = true)
    void reassignPayer(@Param("tripId") UUID tripId, @Param("fromId") UUID fromId, @Param("toId") UUID toId);

    @Modifying
    @Query(value = "UPDATE expense_participants SET participant_id = :toId WHERE participant_id = :fromId AND expense_id IN (SELECT id FROM expenses WHERE trip_id = :tripId)", nativeQuery = true)
    void reassignParticipantShares(@Param("tripId") UUID tripId, @Param("fromId") UUID fromId, @Param("toId") UUID toId);
}
