package com.becool.controller;

import com.becool.service.PasswordRecoveryService;
import com.becool.api.RecoveryApi;
import com.becool.model.AuthResponse;
import com.becool.model.ClientEmailFromPasswordRecovery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PasswordRecoveryController implements RecoveryApi {
    private final PasswordRecoveryService passwordRecoveryService;

    @Override
    public ResponseEntity<AuthResponse> getClientEmail(ClientEmailFromPasswordRecovery clientEmail) {
        return ResponseEntity.ok(passwordRecoveryService.getClientEmail(clientEmail));
    }
}
