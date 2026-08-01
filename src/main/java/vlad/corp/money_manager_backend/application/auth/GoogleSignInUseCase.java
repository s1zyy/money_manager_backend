package vlad.corp.money_manager_backend.application.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.application.exceptions.InvalidCredentialsException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

import java.util.List;
import java.util.UUID;

public class GoogleSignInUseCase {

    private final ParticipantRepository participantRepository;
    private final TokenGenerator tokenGenerator;
    private final List<String> googleClientIds;

    public GoogleSignInUseCase(ParticipantRepository participantRepository,
                               TokenGenerator tokenGenerator,
                               List<String> googleClientIds) {
        this.participantRepository = participantRepository;
        this.tokenGenerator = tokenGenerator;
        this.googleClientIds = googleClientIds;
    }

    public LoginResult execute(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(googleClientIds)
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new InvalidCredentialsException("Invalid Google token");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            if (name == null || name.isBlank()) {
                name = email.split("@")[0];
            }
            final String participantName = name;

            Participant participant = participantRepository.findByEmail(email)
                    .orElseGet(() -> {
                        Participant newP = new Participant(UUID.randomUUID(), participantName, email, null, false);
                        participantRepository.save(newP);
                        return newP;
                    });

            String token = tokenGenerator.generateToken(participant, List.of("ROLE_USER"));
            return new LoginResult(token, participant.getName(), participant.getEmail());

        } catch (InvalidCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCredentialsException("Failed to verify Google token");
        }
    }
}
