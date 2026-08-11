package vlad.corp.money_manager_backend.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.auth.AppleSignInUseCase;
import vlad.corp.money_manager_backend.application.auth.ForgotPasswordUseCase;
import vlad.corp.money_manager_backend.application.auth.GoogleSignInUseCase;
import vlad.corp.money_manager_backend.application.auth.LoginUseCase;
import vlad.corp.money_manager_backend.application.auth.RegisterUseCase;
import vlad.corp.money_manager_backend.application.auth.ResetPasswordUseCase;
import vlad.corp.money_manager_backend.application.invite.InviteTokenInfo;
import vlad.corp.money_manager_backend.application.invite.ClaimInviteWithLoginUseCase;
import vlad.corp.money_manager_backend.application.invite.ValidateInviteTokenUseCase;
import vlad.corp.money_manager_backend.presentation.dto.auth.AuthRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.ClaimInviteWithLoginRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.AppleAuthRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.ForgotPasswordRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.GoogleAuthRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.LoginRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.LoginResponse;
import vlad.corp.money_manager_backend.presentation.dto.auth.ResetPasswordRequest;
import vlad.corp.money_manager_backend.presentation.dto.invite.InviteTokenInfoResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final GoogleSignInUseCase googleSignInUseCase;
    private final AppleSignInUseCase appleSignInUseCase;
    private final ValidateInviteTokenUseCase validateInviteTokenUseCase;
    private final ClaimInviteWithLoginUseCase claimInviteWithLoginUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public AuthController(RegisterUseCase registerUseCase,
                          LoginUseCase loginUseCase,
                          GoogleSignInUseCase googleSignInUseCase,
                          AppleSignInUseCase appleSignInUseCase,
                          ValidateInviteTokenUseCase validateInviteTokenUseCase,
                          ClaimInviteWithLoginUseCase claimInviteWithLoginUseCase,
                          ForgotPasswordUseCase forgotPasswordUseCase,
                          ResetPasswordUseCase resetPasswordUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
        this.googleSignInUseCase = googleSignInUseCase;
        this.appleSignInUseCase = appleSignInUseCase;
        this.validateInviteTokenUseCase = validateInviteTokenUseCase;
        this.claimInviteWithLoginUseCase = claimInviteWithLoginUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @PostMapping("/register")
    public LoginResponse register(@Valid @RequestBody AuthRequest authRequest) {
        String token = registerUseCase.register(
                authRequest.email(), authRequest.password(), authRequest.name(), authRequest.inviteToken());
        return new LoginResponse(token, authRequest.name(), authRequest.email());
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        var result = loginUseCase.login(loginRequest.email(), loginRequest.password());
        return new LoginResponse(result.token(), result.name(), result.email());
    }

    @GetMapping("/validate-invite")
    public InviteTokenInfoResponse validateInvite(@RequestParam String token) {
        InviteTokenInfo info = validateInviteTokenUseCase.execute(token);
        return new InviteTokenInfoResponse(info.tripName(), info.participantName(), info.invitedEmail(), info.requiresLogin());
    }

    @PostMapping("/google")
    public LoginResponse googleSignIn(@RequestBody GoogleAuthRequest request) {
        var result = googleSignInUseCase.execute(request.idToken());
        return new LoginResponse(result.token(), result.name(), result.email());
    }

    @PostMapping("/apple")
    public LoginResponse appleSignIn(@RequestBody AppleAuthRequest request) {
        var result = appleSignInUseCase.execute(request.identityToken(), request.name());
        return new LoginResponse(result.token(), result.name(), result.email());
    }

    @PostMapping("/claim-invite-login")
    public LoginResponse claimInviteWithLogin(@Valid @RequestBody ClaimInviteWithLoginRequest request) {
        String token = claimInviteWithLoginUseCase.execute(request.token(), request.password());
        return new LoginResponse(token, "", "");
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        forgotPasswordUseCase.execute(request.email());
    }

    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request.token(), request.newPassword());
    }
}
