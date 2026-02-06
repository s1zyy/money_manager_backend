package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TripRepositoryImpl implements TripRepository {
    private final TripJpaRepository tripJpaRepository;
    private final TripMapperEntity tripMapper;

    public TripRepositoryImpl(TripJpaRepository tripJpaRepository, TripMapperEntity tripMapper) {
        this.tripJpaRepository = tripJpaRepository;
        this.tripMapper = tripMapper;
    }

    @Override
    public void save(Trip trip) {
        TripEntity tripEntity = tripMapper.toEntity(trip);
        tripJpaRepository.save(tripEntity);
    }

    @Override
    public Optional<Trip> findById(UUID id) {
        return tripJpaRepository.findById(id)
                .map(tripMapper::toDomain);
    }

    @Override
    public List<Trip> findAll() {
        return tripJpaRepository.findAll().stream()
                .map(tripMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Trip trip) {
        tripJpaRepository.delete(tripMapper.toEntity(trip));
    }

    @Override
    public Optional<Trip> findByJoinCode(JoinCode joinCode) {
        return tripJpaRepository.findByJoinCode(joinCode.value())
                .map(tripMapper::toDomain);
    }

    @Override
    public List<Trip> findAllTripsForUser(UUID userId) {
        return tripJpaRepository.findAllTripsForUser(userId)
                .stream()
                .map(tripMapper::toDomain)
                .toList();

    }
}
