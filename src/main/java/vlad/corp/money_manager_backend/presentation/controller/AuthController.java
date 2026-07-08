package vlad.corp.money_manager_backend.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.auth.LoginUseCase;
import vlad.corp.money_manager_backend.application.auth.RegisterUseCase;
import vlad.corp.money_manager_backend.application.invite.InviteTokenInfo;
import vlad.corp.money_manager_backend.application.invite.ValidateInviteTokenUseCase;
import vlad.corp.money_manager_backend.presentation.dto.auth.AuthRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.LoginRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.LoginResponse;
import vlad.corp.money_manager_backend.presentation.dto.invite.InviteTokenInfoResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final ValidateInviteTokenUseCase validateInviteTokenUseCase;

    public AuthController(RegisterUseCase registerUseCase,
                          LoginUseCase loginUseCase,
                          ValidateInviteTokenUseCase validateInviteTokenUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
        this.validateInviteTokenUseCase = validateInviteTokenUseCase;
    }

    @PostMapping("/register")
    public LoginResponse register(@Valid @RequestBody AuthRequest authRequest) {
        String token = registerUseCase.register(
                authRequest.email(), authRequest.password(), authRequest.name(), authRequest.inviteToken());
        return new LoginResponse(token);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = loginUseCase.login(loginRequest.email(), loginRequest.password());
        return new LoginResponse(token);
    }

    @GetMapping("/validate-invite")
    public InviteTokenInfoResponse validateInvite(@RequestParam String token) {
        InviteTokenInfo info = validateInviteTokenUseCase.execute(token);
        return new InviteTokenInfoResponse(info.tripName(), info.participantName(), info.invitedEmail());
    }
}
