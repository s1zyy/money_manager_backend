package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TripJpaRepository extends JpaRepository<TripEntity, UUID> {
}
