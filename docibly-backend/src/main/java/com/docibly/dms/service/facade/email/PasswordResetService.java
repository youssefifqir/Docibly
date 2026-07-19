package com.docibly.dms.service.facade.email;

public interface PasswordResetService {

    void initiateForgotPassword(String email);

    void resetPassword(String token, String newPassword, String confirmNewPassword);

    void sendVerificationEmail(String userId);

    void verifyEmail(String token);

}
