package com.docibly.dms.service.impl.email;

import com.docibly.dms.config.email.EmailProperties;
import com.docibly.dms.service.facade.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

    @Override
    @Async
    public void sendPasswordResetEmail(final String toEmail, final String firstName, final String resetToken) {
        final String subject = "Reset your password";
        final String body = buildPasswordResetBody(firstName, resetToken);
        send(toEmail, subject, body);
    }

    @Override
    @Async
    public void sendEmailVerificationEmail(final String toEmail, final String firstName, final String verificationToken) {
        final String subject = "Verify your email address";
        final String body = buildEmailVerificationBody(firstName, verificationToken);
        send(toEmail, subject, body);
    }

    @Override
    @Async
    public void sendInvitationEmail(final String toEmail, final String orgName, final String invitedByEmail, final String token) {
        final String inviteLink = "%s/invite/%s".formatted(this.emailProperties.getFrontendUrl(), token);
        final String subject = "You're invited to join " + orgName;
        final String body = buildInvitationBody(orgName, invitedByEmail, inviteLink);
        send(toEmail, subject, body);
    }

    private void send(final String to, final String subject, final String htmlBody) {
        try {
            final MimeMessage message = this.mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(this.emailProperties.getFromAddress(), this.emailProperties.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            this.mailSender.send(message);
            log.debug("Email sent to {}: {}", to, subject);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    private String buildPasswordResetBody(final String firstName, final String token) {
        return """
                <html><body style="font-family:sans-serif;color:#222">
                <h2>Hi %s,</h2>
                <p>We received a request to reset your password.</p>
                <p>Use the token below in the reset password form (valid for %d minutes):</p>
                <p style="font-size:1.4em;font-weight:bold;letter-spacing:2px">%s</p>
                <p>If you did not request a password reset, ignore this email.</p>
                </body></html>
                """.formatted(firstName, this.emailProperties.getResetTokenExpiryMinutes(), token);
    }

    private String buildEmailVerificationBody(final String firstName, final String token) {
        final String link = "%s/verify-email?token=%s".formatted(
                this.emailProperties.getFrontendUrl(), token);
        return """
                <html><body style="font-family:sans-serif;color:#222">
                <h2>Hi %s,</h2>
                <p>Thanks for registering! Please verify your email address.</p>
                <p>Click the link below (valid for %d hours):</p>
                <p><a href="%s" style="font-size:1.1em;color:#2563eb">%s</a></p>
                </body></html>
                """.formatted(firstName, this.emailProperties.getVerificationTokenExpiryHours(), link, link);
    }

    private String buildInvitationBody(final String orgName, final String invitedByEmail, final String inviteLink) {
        return """
                <html><body style="font-family:sans-serif;color:#222">
                <h2>You're invited!</h2>
                <p><strong>%s</strong> invited you to join <strong>%s</strong>.</p>
                <p>Click the link below to accept the invitation (valid for 7 days):</p>
                <p><a href="%s" style="display:inline-block;padding:12px 28px;background:#e8622a;color:#fff;text-decoration:none;border-radius:6px;font-size:1em">Accept Invitation</a></p>
                <p style="margin-top:24px;font-size:0.85em;color:#888">Or copy this link into your browser:<br><span style="color:#2563eb">%s</span></p>
                </body></html>
                """.formatted(invitedByEmail, orgName, inviteLink, inviteLink);
    }

}
