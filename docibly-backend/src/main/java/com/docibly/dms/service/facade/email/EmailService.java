package com.docibly.dms.service.facade.email;

public interface EmailService {

    void sendPasswordResetEmail(String toEmail, String firstName, String resetToken);

    void sendEmailVerificationEmail(String toEmail, String firstName, String verificationToken);

    void sendInvitationEmail(String toEmail, String orgName, String invitedByEmail, String token);

}
