package vlad.corp.money_manager_backend.application.auth;

public record LoginResult(String token, String name, String email) {}
