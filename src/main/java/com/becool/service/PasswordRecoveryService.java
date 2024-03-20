package com.becool.service;

import com.becool.generator.PasswordGenerator;
import com.becool.model.EmailSendingRequest;
import com.becool.db.tables.pojos.Authentication;
import com.becool.exception.AuthException;
import com.becool.model.*;
import com.becool.model.AuthResponse;
import com.becool.model.ClientEmailFromPasswordRecovery;
import com.becool.repository.AuthenticationRepository;
import com.becool.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {
    private final EmailSender emailSender;
    private final PasswordGenerator passwordGenerator;
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    @Transactional
    public AuthResponse getClientEmail(ClientEmailFromPasswordRecovery clientEmail) {

        if (authenticationRepository.getAuthByLogin(clientEmail.getEmail()).isPresent()) {

            final Authentication user = authenticationRepository.getAuthByLogin(clientEmail.getEmail())
                    .orElseThrow(() -> new AuthException("Пользователь не найден"));

            String newPassword = passwordGenerator.generateRandomPassword(10);
            authenticationRepository.updatePassword(user.getId(), passwordEncoder.encode(newPassword));
            tokenRepository.cleanToken(user.getId());

            String recipient = user.getLogin();
            String subject = "Psychotech: Recovery password";
            String text = "Ваш пароль сброшен. \n Новый пароль: " + newPassword;

            emailSender.sendEmail(new EmailSendingRequest(recipient, subject, text));

            return new AuthResponse().id(user.getId().toString()).email(user.getLogin());
        }
        else { throw new AuthException("Пользователь не существует"); }
    }
}
