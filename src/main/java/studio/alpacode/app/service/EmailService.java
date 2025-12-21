package studio.alpacode.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${spring.mail.host:}")
    private String mailHost;

    public void sendInviteEmail(String toEmail, String name, String inviteToken) {
        String inviteLink = baseUrl + "/invite/" + inviteToken;

        String subject = "Sei stato invitato a Alpacode Dashboard";
        String body = String.format("""
            Ciao %s,

            Sei stato invitato a unirti ad Alpacode Dashboard.

            Clicca sul seguente link per impostare la tua password e attivare il tuo account:
            %s

            Questo link scadrà tra 48 ore.

            Se non hai richiesto questo invito, puoi ignorare questa email.

            Cordiali saluti,
            Il team Alpacode
            """, name, inviteLink);

        if (mailHost == null || mailHost.isBlank()) {
            // Mock mode - just log the email
            log.info("========================================");
            log.info("MOCK EMAIL - Non inviata (mail server non configurato)");
            log.info("========================================");
            log.info("To: {}", toEmail);
            log.info("Subject: {}", subject);
            log.info("Body:\n{}", body);
            log.info("========================================");
            log.info("INVITE LINK: {}", inviteLink);
            log.info("========================================");
        } else {
            // TODO: Implement actual email sending with Spring Mail
            log.info("Sending invite email to: {}", toEmail);
            log.info("Invite link: {}", inviteLink);
            // For now, still log the link for convenience
        }
    }

    public void sendPasswordResetEmail(String toEmail, String name, String resetToken) {
        String resetLink = baseUrl + "/reset-password/" + resetToken;

        String subject = "Reset della password - Alpacode Dashboard";
        String body = String.format("""
            Ciao %s,

            Hai richiesto il reset della tua password.

            Clicca sul seguente link per reimpostare la password:
            %s

            Questo link scadrà tra 1 ora.

            Se non hai richiesto questo reset, puoi ignorare questa email.

            Cordiali saluti,
            Il team Alpacode
            """, name, resetLink);

        if (mailHost == null || mailHost.isBlank()) {
            log.info("========================================");
            log.info("MOCK EMAIL - Non inviata (mail server non configurato)");
            log.info("========================================");
            log.info("To: {}", toEmail);
            log.info("Subject: {}", subject);
            log.info("Body:\n{}", body);
            log.info("========================================");
        } else {
            log.info("Sending password reset email to: {}", toEmail);
        }
    }
}
