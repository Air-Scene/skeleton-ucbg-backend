package com.example.demo.email.service;

import com.example.demo.account.model.AccountLanguage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource messageSource;

    @Value("${app.backend-url}")
    private String backendUrl;

    @Value("${spring.mail.from-email:noreply@localhost}")
    private String fromEmail;

    public void sendVerificationEmail(String to, String token, AccountLanguage language) throws MessagingException {
        String languageString = language.toString();
        Context context = new Context(Locale.of(languageString));
        String verificationUrl = backendUrl + "/api/auth/verify?token=" + token + "&language=" + languageString;
        
        context.setVariables(Map.of(
            "verificationUrl", verificationUrl
        ));
        
        String content = templateEngine.process("email/verification", context);
        String subject = messageSource.getMessage("email.verification.subject", null, Locale.of(languageString));
        sendEmail(to, subject, content);
    }

    public void sendPasswordResetEmail(String to, String token, AccountLanguage language) throws MessagingException {
        String languageString = language.toString();
        Context context = new Context(Locale.of(languageString));
        String resetUrl = backendUrl + "/api/auth/reset-password/validate?token=" + token + "&language=" + languageString;
        
        context.setVariables(Map.of(
            "resetUrl", resetUrl
        ));
        
        String content = templateEngine.process("email/password-reset", context);
        String subject = messageSource.getMessage("email.passwordReset.subject", null, Locale.of(languageString));
        sendEmail(to, subject, content);
    }

    private void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        try {
            mailSender.send(mimeMessage);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}