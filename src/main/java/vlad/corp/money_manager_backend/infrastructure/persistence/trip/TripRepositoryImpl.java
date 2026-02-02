package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TripRepositoryImpl implements TripRepository {

    @Override
    public void save(Trip trip) {
    }

    @Override
    public Optional<Trip> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Trip> findAll() {
        return List.of();
    }

    @Override
    public void delete(Trip trip) {

    }
}
