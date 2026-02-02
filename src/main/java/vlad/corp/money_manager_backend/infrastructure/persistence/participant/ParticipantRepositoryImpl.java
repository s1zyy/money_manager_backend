package vlad.corp.money_manager_backend.infrastructure.persistence.participant;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ParticipantRepositoryImpl implements ParticipantRepository {
    @Override
    public void save(Participant participant) {

    }

    @Override
    public Optional<Participant> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void delete(Participant participant) {

    }

    @Override
    public Optional<Participant> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Participant> findAll() {
        return List.of();
    }
}
