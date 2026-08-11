package vlad.corp.money_manager_backend.infrastructure.persistence.participant;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public Optional<Participant> findById(UUID id) {
        return jpaRepository.findById(id).map(participantMapper::toDomain);
    }

    @Override
    public Set<Participant> findAllByIds(Set<UUID> ids) {
        if (ids.isEmpty()) {
            return new HashSet<>();
        }
        return jpaRepository.findAllById(ids)
                .stream()
                .map(participantMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void softDelete(UUID id) {
        jpaRepository.softDeleteById(id, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void updatePassword(UUID id, String newPassword) {
        jpaRepository.updatePasswordById(id, newPassword);
    }
}
