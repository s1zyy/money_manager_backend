package vlad.corp.money_manager_backend.presentation.controller;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.wallet.*;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedUser;
import vlad.corp.money_manager_backend.presentation.dto.wallet.CreateWalletRequest;
import vlad.corp.money_manager_backend.presentation.dto.wallet.JoinWalletRequest;
import vlad.corp.money_manager_backend.presentation.dto.wallet.WalletDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    
    private final CreateWalletUseCase createWalletUseCase;
    private final JoinWalletByCodeUseCase joinWalletByCodeUseCase;
    private final ListMyWalletsUseCase listMyWalletsUseCase;
    private final GetWalletByIdUseCase getWalletByIdUseCase;

    public WalletController(
            CreateWalletUseCase createWalletUseCase,
            JoinWalletByCodeUseCase joinWalletByCodeUseCase, ListMyWalletsUseCase listMyWalletsUseCase, GetWalletByIdUseCase getWalletByIdUseCase
    ) {
        this.createWalletUseCase = createWalletUseCase;
        this.joinWalletByCodeUseCase = joinWalletByCodeUseCase;
        this.listMyWalletsUseCase = listMyWalletsUseCase;
        this.getWalletByIdUseCase = getWalletByIdUseCase;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDto create(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody CreateWalletRequest request
    ) {
        CreateWalletResult result = createWalletUseCase.execute(user.userId(), request.name());

        return new WalletDto(
                result.walletId(),
                result.ownerId(),
                result.name(),
                result.joinCode(),
                result.currencyCode(),
                result.members(),
                result.createdAt()
        );
    }
    
    @PostMapping("/join")
    public UUID join(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody JoinWalletRequest code
    ){
        return joinWalletByCodeUseCase.execute(user.userId(), code.code());
    }
    
    @GetMapping("/myWallets")
    public List<WalletDto> myWallets(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return listMyWalletsUseCase.execute(user.userId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{walletId}")
    public WalletDto getWalletById(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID walletId
    ) {
        return toDto(getWalletByIdUseCase.execute(user.userId(), walletId));
    }
    
    private WalletDto toDto(Wallet wallet) {
        return new WalletDto(
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
