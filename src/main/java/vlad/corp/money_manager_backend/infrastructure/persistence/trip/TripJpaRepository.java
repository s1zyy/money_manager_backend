package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripJpaRepository extends JpaRepository<TripEntity, UUID> {
    Optional<TripEntity> findByJoinCode(String joinCode);
    @Query("SELECT t FROM TripEntity t JOIN t.participantIds p WHERE p = :userId")
    List<TripEntity> findAllTripsForUser(@Param("userId") UUID userId);

    @Query("SELECT t FROM TripEntity t WHERE t.tripStatus.id = 1 AND t.startDate <= :today")
    List<TripEntity> findUpcomingTripsStartingByDate(@Param("today") LocalDate today);
}
