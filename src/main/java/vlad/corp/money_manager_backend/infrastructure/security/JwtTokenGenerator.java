package vlad.corp.money_manager_backend.infrastructure.security;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.domain.model.User;
import java.util.List;

@Component
public class JwtTokenGenerator implements TokenGenerator {

    private final JwtUtil jwtUtil;

    public JwtTokenGenerator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String generateToken(User user, List<String> roles) {
        return jwtUtil.generateToken(user, roles);
    }
}
