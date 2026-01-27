package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.wallet.*;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

@Configuration
public class WalletUseCaseConfig {

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
    public GetWalletByIdUseCase getWalletByIdUseCase(WalletRepository walletRepository) {
        return new GetWalletByIdUseCase(walletRepository);
    }

    @Bean
    public ListMyWalletsUseCase listMyWalletsUseCase(WalletRepository walletRepository) {
        return new ListMyWalletsUseCase(walletRepository);
    }



}
