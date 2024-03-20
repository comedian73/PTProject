package com.becool.mapper;

import com.becool.model.NewClient;
import com.becool.util.UuidV6;
import com.becool.model.ClientRegistrationRequest;

public class ClientMapper {

    public static NewClient mapNewClient(ClientRegistrationRequest auth) {
        return NewClient.builder()
                .id(UuidV6.generate(false).getUuid())
                .name(auth.getName())
                .email(auth.getEmail())
                .build(
        );
    }
}
