package vlad.corp.money_manager_backend.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vlad.corp.money_manager_backend.application.auth.LoginUseCase;
import vlad.corp.money_manager_backend.application.auth.RegisterUseCase;
import vlad.corp.money_manager_backend.presentation.dto.auth.AuthRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.LoginRequest;
import vlad.corp.money_manager_backend.presentation.dto.auth.LoginResponse;

@RestController
@RequestMapping
public class AuthController {
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUseCase registerUseCase, LoginUseCase loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody AuthRequest authRequest) {
        String token = registerUseCase.register(authRequest.email(), authRequest.password());
        return new LoginResponse(token);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        String token = loginUseCase.login(loginRequest.email(), loginRequest.password());
        return new LoginResponse(token);
    }
}
