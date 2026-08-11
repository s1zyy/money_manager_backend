package vlad.corp.money_manager_backend.infrastructure.persistence.reset;

import jakarta.transaction.Transactional;
import vlad.corp.money_manager_backend.domain.model.PasswordResetToken;
import vlad.corp.money_manager_backend.domain.repository.PasswordResetTokenRepository;

import java.util.Optional;
import java.util.UUID;

public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final PasswordResetTokenJpaRepository jpa;

    public PasswordResetTokenRepositoryImpl(PasswordResetTokenJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(PasswordResetToken token) {
        jpa.save(PasswordResetTokenEntity.builder()
                .token(token.token())
                .participantId(token.participantId())
                .expiresAt(token.expiresAt())
                .build());
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpa.findByToken(token)
                .map(e -> new PasswordResetToken(e.getToken(), e.getParticipantId(), e.getExpiresAt()));
    }

    @Override
    @Transactional
    public void deleteByParticipantId(UUID participantId) {
        jpa.deleteByParticipantId(participantId);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        jpa.deleteById(token);
    }
}
