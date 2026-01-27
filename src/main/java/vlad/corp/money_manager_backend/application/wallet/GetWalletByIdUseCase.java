package vlad.corp.money_manager_backend.application.wallet;

import vlad.corp.money_manager_backend.application.exception.AccessDeniedException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.util.UUID;

public class GetWalletByIdUseCase {

    private final WalletRepository walletRepository;

    public GetWalletByIdUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet execute(UUID userId, UUID walletId){
        Wallet wallet= walletRepository.findById(walletId)
                .orElseThrow(()-> new NotFoundException("Wallet not found with this id"));
        if(!wallet.isMember(userId)){
            throw new AccessDeniedException("User is not a member of this wallet");
        }
        return wallet;
    }
}
