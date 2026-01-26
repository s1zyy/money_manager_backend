package vlad.corp.money_manager_backend.application.wallet;

import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.util.UUID;

public class JoinWalletByCodeUseCase {

    private final WalletRepository walletRepository;

    public JoinWalletByCodeUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void execute(UUID userId, String code){
        JoinCode joinCode = JoinCode.of(code);

        Wallet wallet = walletRepository.findByJoinCode(joinCode)
                .orElseThrow(()-> new IllegalArgumentException("Wallet not found by join code"));

        wallet.join(userId);
        walletRepository.save(wallet);
    }
}
