package vlad.corp.money_manager_backend.application.auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.application.exceptions.InvalidCredentialsException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AppleSignInUseCase {

    private final ParticipantRepository participantRepository;
    private final TokenGenerator tokenGenerator;
    private final String bundleId;

    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";
    private static final String APPLE_ISSUER = "https://appleid.apple.com";

    public AppleSignInUseCase(ParticipantRepository participantRepository,
                              TokenGenerator tokenGenerator,
                              String bundleId) {
        this.participantRepository = participantRepository;
        this.tokenGenerator = tokenGenerator;
        this.bundleId = bundleId;
    }

    public LoginResult execute(String identityToken, String name) {
        try {
            SignedJWT jwt = SignedJWT.parse(identityToken);
            String kid = jwt.getHeader().getKeyID();

            JWKSet jwkSet = JWKSet.load(URI.create(APPLE_KEYS_URL).toURL());
            RSAKey rsaKey = (RSAKey) jwkSet.getKeyByKeyId(kid);
            if (rsaKey == null) throw new InvalidCredentialsException("Apple key not found");

            if (!jwt.verify(new RSASSAVerifier(rsaKey))) {
                throw new InvalidCredentialsException("Invalid Apple token signature");
            }

            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            if (!APPLE_ISSUER.equals(claims.getIssuer())) {
                throw new InvalidCredentialsException("Invalid Apple token issuer");
            }
            if (!claims.getAudience().contains(bundleId)) {
                throw new InvalidCredentialsException("Invalid Apple token audience");
            }
            if (claims.getExpirationTime().before(new Date())) {
                throw new InvalidCredentialsException("Apple token expired");
            }

            String email = claims.getStringClaim("email");
            String sub = claims.getSubject();

            if (email == null || email.isBlank()) {
                email = sub + "@privaterelay.appleid.com";
            }

            final String participantEmail = email;
            final String participantName = (name != null && !name.isBlank()) ? name : email.split("@")[0];

            Participant participant = participantRepository.findByEmail(participantEmail)
                    .orElseGet(() -> {
                        Participant newP = new Participant(UUID.randomUUID(), participantName, participantEmail, null, false);
                        participantRepository.save(newP);
                        return newP;
                    });

            String token = tokenGenerator.generateToken(participant, List.of("ROLE_USER"));
            return new LoginResult(token, participant.getName(), participant.getEmail());

        } catch (InvalidCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCredentialsException("Failed to verify Apple token");
        }
    }
}
