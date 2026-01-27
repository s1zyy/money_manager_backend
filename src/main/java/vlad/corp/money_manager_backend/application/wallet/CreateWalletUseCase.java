package vlad.corp.money_manager_backend.application.wallet;

import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.util.UUID;

public class CreateWalletUseCase {

    private static final String DEFAULT_CURRENCY = "EUR";

    private final WalletRepository walletRepository;
    private final JoinCodeGenerator joinCodeGenerator;

    public CreateWalletUseCase(WalletRepository walletRepository, JoinCodeGenerator joinCodeGenerator) {
        this.walletRepository = walletRepository;
        this.joinCodeGenerator = joinCodeGenerator;
    }

    public CreateWalletResult execute(UUID ownerId, String name) {
        JoinCode joinCode = joinCodeGenerator.generate();
        Wallet wallet = Wallet.create(ownerId, name, joinCode, DEFAULT_CURRENCY);
        walletRepository.save(wallet);
        return new CreateWalletResult(
                wallet.getWalletId(),
                wallet.getOwnerId(),
                wallet.getName(),
                wallet.getJoinCode().value(),
                wallet.getCurrencyCode(),
                wallet.getMembers(),
                wallet.getCreatedAt()
        );
    }
}
