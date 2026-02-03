package vlad.corp.money_manager_backend.infrastructure.persistence.participant;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ParticipantRepositoryImpl implements ParticipantRepository {
    private final ParticipantJpaRepository jpaRepository;
    private final ParticipantMapper participantMapper;

    public ParticipantRepositoryImpl(ParticipantJpaRepository jpaRepository, ParticipantMapper participantMapper) {
        this.jpaRepository = jpaRepository;
        this.participantMapper = participantMapper;
    }

    @Override
    public void save(Participant participant) {
        jpaRepository.save(participantMapper.toEntity(participant));
    }

    @Override
    public Optional<Participant> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(participantMapper::toDomain);
    }

    @Override
    public void delete(Participant participant) {
        jpaRepository.delete(participantMapper.toEntity(participant));

    }

    @Override
    public Optional<Participant> findById(UUID id) {
        return jpaRepository.findById(id).map(participantMapper::toDomain);
    }

    @Override
    public List<Participant> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(participantMapper::toDomain)
                .toList();
    }
}
