package vlad.corp.money_manager_backend.infrastructure.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.transaction.AddTransactionUseCase;
import vlad.corp.money_manager_backend.application.transaction.UpdateTransactionUseCase;
import vlad.corp.money_manager_backend.domain.repository.TransactionRepository;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;
import vlad.corp.money_manager_backend.application.transaction.ListTransactionsByWalletUseCase;

@Configuration
public class TransactionUseCaseConfig {


    @Bean
    public AddTransactionUseCase addTransactionUseCase(
            TransactionRepository txRepository, WalletRepository walletRepository) {
        return new AddTransactionUseCase(txRepository, walletRepository);
    }

    @Bean
    public UpdateTransactionUseCase updateTransactionUseCase(
            TransactionRepository txRepository, WalletRepository walletRepository){
        return new UpdateTransactionUseCase(txRepository, walletRepository);
    }

    @Bean
    public ListTransactionsByWalletUseCase listTransactionsByWalletUseCase(
            TransactionRepository txRepository, WalletRepository walletRepository){
        return new ListTransactionsByWalletUseCase(txRepository, walletRepository);
    }
}
