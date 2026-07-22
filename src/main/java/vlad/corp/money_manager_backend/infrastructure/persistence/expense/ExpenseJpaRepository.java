package vlad.corp.money_manager_backend.infrastructure.persistence.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Set;
import java.util.UUID;


public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, UUID> {
    @Query("SELECT DISTINCT e FROM ExpenseEntity e LEFT JOIN FETCH e.participantShares WHERE e.tripId = :id")
    Set<ExpenseEntity> findAllByTripId(@Param("id") UUID id);
    @Query(value = """
            SELECT DISTINCT participant_id FROM expense_participants
            WHERE expense_id IN (SELECT id FROM expenses WHERE trip_id = :tripId)
            UNION
            SELECT DISTINCT payer_id FROM expenses
            WHERE trip_id = :tripId AND payer_id IS NOT NULL
            """, nativeQuery = true)
    Set<UUID> findActiveParticipantIds(@Param("tripId") UUID tripId);
    @Query(value = "SELECT (EXISTS(SELECT 1 FROM expenses WHERE payer_id = :id) OR EXISTS(SELECT 1 FROM expense_participants WHERE participant_id = :id))", nativeQuery = true)
    boolean existsAnyExpenseInvolvement(@Param("id") UUID participantId);

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
