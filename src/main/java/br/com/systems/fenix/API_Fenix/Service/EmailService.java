package br.com.systems.fenix.API_Fenix.Service;

public interface EmailService {
    void sendSimpleMailMessage(String name, String to, String token);

    void sendSimpleMailMessageResetPassword(String name, String to, String token, String subject);

    void sendMineMessageWithAttachments(String name, String to, String token);

    void sendMineMessageWithEmbeddedFiles(String name, String to, String token);

    void sendHtmlEmail(String name, String to, String token);

    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);
}
