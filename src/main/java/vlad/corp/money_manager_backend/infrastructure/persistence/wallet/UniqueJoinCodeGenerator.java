package vlad.corp.money_manager_backend.infrastructure.persistence.wallet;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.application.wallet.JoinCodeGenerator;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.security.SecureRandom;
@Component
public class UniqueJoinCodeGenerator implements JoinCodeGenerator {

    private static final int MAX_ATTEMPTS = 50;
    private final SecureRandom random = new SecureRandom();
    private final WalletRepository walletRepository;

    public UniqueJoinCodeGenerator(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public JoinCode generate() {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            String code = String.format("%06d", random.nextInt(100_000_000));
            JoinCode joinCode = JoinCode.of(code);
            if(walletRepository.findByJoinCode(joinCode).isEmpty()){
                return joinCode;
            }
        }
        throw new IllegalStateException("Failed to generate unique join code after " + MAX_ATTEMPTS + " attempts");

    }
}
