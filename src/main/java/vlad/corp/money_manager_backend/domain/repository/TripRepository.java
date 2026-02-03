package vlad.corp.money_manager_backend.domain.repository;

import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripRepository {
    void save(Trip trip);
    Optional<Trip> findById(UUID id);
    List<Trip> findAll();
    void delete(Trip trip);
    Optional<Trip> findByJoinCode(JoinCode joinCode);
}
