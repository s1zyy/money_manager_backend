package vlad.corp.money_manager_backend.presentation;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.wallet.CreateWalletResult;
import vlad.corp.money_manager_backend.application.wallet.CreateWalletUseCase;
import vlad.corp.money_manager_backend.application.wallet.GetMyMainWalletUserCase;
import vlad.corp.money_manager_backend.application.wallet.JoinWalletByCodeUseCase;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.presentation.dto.CreateWalletRequest;
import vlad.corp.money_manager_backend.presentation.dto.JoinWalletRequest;
import vlad.corp.money_manager_backend.presentation.dto.WalletDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    
    private final CreateWalletUseCase createWalletUseCase;
    private final JoinWalletByCodeUseCase joinWalletByCodeUseCase;
    private final GetMyMainWalletUserCase getMyMainWalletUserCase;

    public WalletController(
            CreateWalletUseCase createWalletUseCase,
            JoinWalletByCodeUseCase joinWalletByCodeUseCase,
            GetMyMainWalletUserCase getMyMainWalletUserCase) {
        this.createWalletUseCase = createWalletUseCase;
        this.joinWalletByCodeUseCase = joinWalletByCodeUseCase;
        this.getMyMainWalletUserCase = getMyMainWalletUserCase;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDto create(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody CreateWalletRequest request
    ) {
        CreateWalletResult result = createWalletUseCase.execute(userId, request.name());

        return new WalletDto(
                result.walletId(),
                result.ownerId(),
                result.name(),
                result.joinCode(),
                result.members(),
                result.createdAt()
        );
    }
    
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void join(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody JoinWalletRequest code
    ){
        joinWalletByCodeUseCase.execute(userId, code.code());
    }
    
    @GetMapping("/me")
    public WalletDto myWallet(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        return toDto(getMyMainWalletUserCase.execute(userId));
    }
    
    private WalletDto toDto(Wallet wallet) {
        return new WalletDto(
                wallet.getWalletId(),
                wallet.getOwnerId(),
                wallet.getName(),
                wallet.getJoinCode().value(),
                wallet.getMembers(),
                wallet.getCreatedAt()
        );
    }
}
