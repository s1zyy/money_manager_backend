package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.wallet.CreateWalletUseCase;
import vlad.corp.money_manager_backend.application.wallet.GetMyMainWalletUserCase;
import vlad.corp.money_manager_backend.application.wallet.JoinCodeGenerator;
import vlad.corp.money_manager_backend.application.wallet.JoinWalletByCodeUseCase;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateWalletUseCase createWalletUseCase(
            WalletRepository walletRepository,
            JoinCodeGenerator joinCodeGenerator
    ) {
        return new CreateWalletUseCase(walletRepository, joinCodeGenerator);
    }

    @Bean
    public JoinWalletByCodeUseCase joinWalletByCodeUseCase(WalletRepository walletRepository) {
        return new JoinWalletByCodeUseCase(walletRepository);
    }

    @Bean
    public GetMyMainWalletUserCase getMyMainWalletUserCase(WalletRepository walletRepository) {
        return new GetMyMainWalletUserCase(walletRepository);
    }
}
