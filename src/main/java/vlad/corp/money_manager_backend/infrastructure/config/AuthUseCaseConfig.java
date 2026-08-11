package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.AppleSignInUseCase;
import vlad.corp.money_manager_backend.application.auth.ForgotPasswordUseCase;
import vlad.corp.money_manager_backend.application.auth.GoogleSignInUseCase;
import vlad.corp.money_manager_backend.application.auth.LoginUseCase;
import vlad.corp.money_manager_backend.application.auth.RegisterUseCase;
import vlad.corp.money_manager_backend.application.auth.ResetPasswordUseCase;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.application.port.EmailSender;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.PasswordResetTokenRepository;
import vlad.corp.money_manager_backend.domain.repository.VirtualParticipantInviteRepository;
import vlad.corp.money_manager_backend.infrastructure.persistence.reset.PasswordResetTokenJpaRepository;
import vlad.corp.money_manager_backend.infrastructure.persistence.reset.PasswordResetTokenRepositoryImpl;

@Configuration
public class AuthUseCaseConfig {

    @Value("${google.web.client-id}")
    private String googleWebClientId;

    @Value("${google.ios.client-id}")
    private String googleIosClientId;

    @Value("${google.android.client-id}")
    private String googleAndroidClientId;

    @Value("${apple.bundle-id}")
    private String appleBundleId;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisterUseCase registerUseCase(ParticipantRepository participantRepository,
                                           PasswordEncoder encoder,
                                           TokenGenerator tokenGenerator,
                                           VirtualParticipantInviteRepository inviteRepository) {
        return new RegisterUseCase(participantRepository, encoder, tokenGenerator, inviteRepository);
    }

    @Bean
    public LoginUseCase loginUseCase(ParticipantRepository participantRepository,
                                     PasswordEncoder encoder,
                                     TokenGenerator tokenGenerator) {
        return new LoginUseCase(participantRepository, encoder, tokenGenerator);
    }

    @Bean
    public GoogleSignInUseCase googleSignInUseCase(ParticipantRepository participantRepository,
                                                   TokenGenerator tokenGenerator) {
        return new GoogleSignInUseCase(participantRepository, tokenGenerator,
                List.of(googleWebClientId, googleIosClientId, googleAndroidClientId));
    }

    @Bean
    public AppleSignInUseCase appleSignInUseCase(ParticipantRepository participantRepository,
                                                  TokenGenerator tokenGenerator) {
        return new AppleSignInUseCase(participantRepository, tokenGenerator, appleBundleId);
    }

    @Bean
    public PasswordResetTokenRepository passwordResetTokenRepository(PasswordResetTokenJpaRepository jpa) {
        return new PasswordResetTokenRepositoryImpl(jpa);
    }

    @Bean
    public ForgotPasswordUseCase forgotPasswordUseCase(ParticipantRepository participantRepository,
                                                        PasswordResetTokenRepository tokenRepository,
                                                        EmailSender emailSender) {
        return new ForgotPasswordUseCase(participantRepository, tokenRepository, emailSender);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(PasswordResetTokenRepository tokenRepository,
                                                      ParticipantRepository participantRepository) {
        return new ResetPasswordUseCase(tokenRepository, participantRepository);
    }
}
