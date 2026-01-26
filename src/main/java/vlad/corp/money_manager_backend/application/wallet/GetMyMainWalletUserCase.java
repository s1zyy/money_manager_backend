package vlad.corp.money_manager_backend.application.wallet;

import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.util.UUID;

public class GetMyMainWalletUserCase {

    private final WalletRepository walletRepository;

    public GetMyMainWalletUserCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet execute(UUID ownerId){
        return walletRepository.findByOwnerId(ownerId)
                .orElseThrow(()-> new IllegalArgumentException("Wallet not found by ownerId"));
    }
}
