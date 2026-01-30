package vlad.corp.money_manager_backend.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.transaction.AddTransactionUseCase;
import vlad.corp.money_manager_backend.application.transaction.UpdateTransactionUseCase;
import vlad.corp.money_manager_backend.domain.model.Transaction;
import vlad.corp.money_manager_backend.application.transaction.ListTransactionsByWalletUseCase;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedUser;
import vlad.corp.money_manager_backend.presentation.dto.transaction.CreateTransactionRequest;
import vlad.corp.money_manager_backend.presentation.dto.transaction.TransactionDto;
import vlad.corp.money_manager_backend.presentation.dto.transaction.UpdateTransactionRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AddTransactionUseCase addTransactionUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final ListTransactionsByWalletUseCase listTransactionsByWalletUseCase;

    public TransactionController(AddTransactionUseCase addTransactionUseCase,
                                 UpdateTransactionUseCase updateTransactionUseCase,
                                 ListTransactionsByWalletUseCase listTransactionsByWalletUseCase) {
        this.addTransactionUseCase = addTransactionUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.listTransactionsByWalletUseCase = listTransactionsByWalletUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto create(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody CreateTransactionRequest request
    ){
        Transaction tx = addTransactionUseCase.execute(
                request.walletId(),
                user.userId(),
                request.amount(),
                request.category()
        );
        return toDto(tx);
    }

    @PutMapping("/{transactionId}")
    public TransactionDto update(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID transactionId,
            @RequestBody UpdateTransactionRequest request
    ){
        Transaction tx = updateTransactionUseCase.execute(
                user.userId(),
                transactionId,
                request.amount(),
                request.category(),
                request.expectedVersion()
        );
        return toDto(tx);
    }

    @GetMapping
    public List<TransactionDto> listByWallet(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID walletId
    ) {
        return listTransactionsByWalletUseCase.execute(user.userId(),walletId)
                .stream()
                .map(this::toDto)
                .toList();
    }


    private TransactionDto toDto(Transaction tx){
        return new TransactionDto(
                tx.getTransactionId(),
                tx.getWalletId(),
                tx.getAmount().amount(),
                tx.getAmount().currency().getCurrencyCode(),
                tx.getCategory(),
                tx.getVersion(),
                tx.getUpdatedBy(),
                tx.getUpdatedAt()
        );
    }

}
