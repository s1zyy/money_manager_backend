package vlad.corp.money_manager_backend.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.PasswordResetToken;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;

public class ResetPasswordUseCase {

    private final PasswordResetTokenRepository tokenRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordUseCase(PasswordResetTokenRepository tokenRepository,
                                ParticipantRepository participantRepository,
                                PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid or expired reset code"));

        if (resetToken.expiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.deleteByToken(token);
            throw new BusinessException("Reset code has expired");
        }

        participantRepository.findById(resetToken.participantId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        participantRepository.updatePassword(resetToken.participantId(), passwordEncoder.encode(newPassword));
        tokenRepository.deleteByToken(token);
    }
}
