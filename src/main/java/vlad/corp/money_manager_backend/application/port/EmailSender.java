package vlad.corp.money_manager_backend.application.port;

public interface EmailSender {
    void send(String to, String subject, String text);
}
