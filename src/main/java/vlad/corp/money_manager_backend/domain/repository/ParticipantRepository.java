package vlad.corp.money_manager_backend.domain.repository;

import vlad.corp.money_manager_backend.domain.model.Participant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository {
    void save(Participant participant);
    Optional<Participant> findByEmail(String email);
    Optional<Participant> findById(UUID id);
    List<Participant> findAll();
    void delete(Participant participant);
}
