package com.docibly.dms.service.impl.email;

import com.docibly.dms.bean.core.user.PasswordResetToken;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.config.email.EmailProperties;
import com.docibly.dms.dao.facade.email.PasswordResetTokenDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.service.facade.email.EmailService;
import com.docibly.dms.service.facade.email.PasswordResetService;
import com.docibly.dms.bean.core.user.EmailVerificationToken;
import com.docibly.dms.dao.facade.email.EmailVerificationTokenDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.docibly.dms.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserDao userDao;
    private final PasswordResetTokenDao passwordResetTokenDao;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailProperties emailProperties;
    private final EmailVerificationTokenDao emailVerificationTokenDao;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    @Transactional(timeout = 30)
    public void initiateForgotPassword(final String email) {
        final User user = this.userDao.findByEmail(email).orElse(null);
        // Always respond with 202 even if email not found — prevents user enumeration
        if (user == null) {
            log.debug("Password reset requested for unknown email: {}", email);
            return;
        }

        this.passwordResetTokenDao.deleteAllByUserId(user.getId());

        final String rawToken = generateToken();
        final PasswordResetToken token = PasswordResetToken.builder()
                .token(rawToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(this.emailProperties.getResetTokenExpiryMinutes()))
                .used(false)
                .build();

        this.passwordResetTokenDao.save(token);
        this.emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), rawToken);
        log.debug("Password reset token issued for user {}", user.getId());
    }

    @Override
    @Transactional(timeout = 30)
    public void resetPassword(final String rawToken, final String newPassword, final String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new BusinessException(CHANGE_PASSWORD_MISMATCH);
        }

        final PasswordResetToken token = this.passwordResetTokenDao.findByToken(rawToken)
                .orElseThrow(() -> new BusinessException(TOKEN_INVALID));

        if (token.isUsed()) {
            throw new BusinessException(TOKEN_ALREADY_USED);
        }
        if (token.isExpired()) {
            throw new BusinessException(TOKEN_EXPIRED);
        }

        final User user = token.getUser();
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.userDao.save(user);

        token.setUsed(true);
        this.passwordResetTokenDao.save(token);
        log.debug("Password reset completed for user {}", user.getId());
    }

    @Override
    @Transactional(timeout = 30)
    public void sendVerificationEmail(final String userId) {
        final User user = this.userDao.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        this.emailVerificationTokenDao.deleteAllByUserId(userId);

        final String rawToken = generateToken();
        final EmailVerificationToken token = EmailVerificationToken.builder()
                .token(rawToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(this.emailProperties.getVerificationTokenExpiryHours()))
                .used(false)
                .build();

        this.emailVerificationTokenDao.save(token);
        this.emailService.sendEmailVerificationEmail(user.getEmail(), user.getFirstName(), rawToken);
    }

    @Override
    @Transactional(timeout = 30)
    public void verifyEmail(final String rawToken) {
        final EmailVerificationToken token = this.emailVerificationTokenDao.findByToken(rawToken)
                .orElseThrow(() -> new BusinessException(TOKEN_INVALID));

        log.debug("verifyEmail: token={}, expiresAt={}, used={}, now={}",
                rawToken, token.getExpiresAt(), token.isUsed(), LocalDateTime.now());

        if (token.isUsed()) {
            throw new BusinessException(TOKEN_ALREADY_USED);
        }

        final User user = token.getUser();
        user.setEmailVerified(true);
        this.userDao.save(user);

        token.setUsed(true);
        this.emailVerificationTokenDao.save(token);
        log.debug("Email verified for user {}", user.getId());
    }

    private String generateToken() {
        final byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
