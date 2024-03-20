package com.becool.controller;

import com.becool.service.RegistrationService;
import com.becool.api.ClientsApi;
import com.becool.model.ClientRegistrationRequest;
import com.becool.model.ClientRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController implements ClientsApi {
    private final RegistrationService registrationService;

    @Override
    public ResponseEntity<ClientRegistrationResponse> createNewClient(ClientRegistrationRequest request) {
        return ResponseEntity.ok(registrationService.createNewClient(request));
    }
}
