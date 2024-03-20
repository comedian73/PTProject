package com.becool.service;

import com.becool.model.NewClient;
import com.becool.repository.ClientRepository;
import com.becool.exception.RegistrationException;
import com.becool.mapper.AuthMapper;
import com.becool.mapper.ClientMapper;
import com.becool.model.ClientRegistrationRequest;
import com.becool.model.ClientRegistrationResponse;
import com.becool.model.ClientResponse;
import com.becool.repository.AuthenticationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final ClientRepository clientRepository;
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClientRegistrationResponse createNewClient(@NonNull ClientRegistrationRequest regRequest) {

        if (authenticationRepository.getAuthByLogin(regRequest.getEmail()).isEmpty()) {

            NewClient client = ClientMapper.mapNewClient(regRequest);
            client.setPassword(passwordEncoder.encode(regRequest.getPassword()));
            clientRepository.createNewClient(client);
            authenticationRepository.createAuth(AuthMapper.mapAuth(client));

            ClientResponse clientResponse = new ClientResponse();
            clientResponse.setId(client.getId().toString());
            clientResponse.setName(client.getName());
            clientResponse.setEmail(client.getEmail());

            return new ClientRegistrationResponse().client(clientResponse);
        }
        else {
            throw new RegistrationException("Пользователь с таким Email существует");
        }
    }

}
