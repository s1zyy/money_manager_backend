package vlad.corp.money_manager_backend.application.wallet;

import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.util.List;
import java.util.UUID;

public class ListMyWalletsUseCase {

    private final WalletRepository walletRepository;

    public ListMyWalletsUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public List<Wallet> execute(UUID userId){
        return walletRepository.findByMemberId(userId);
    }
}
