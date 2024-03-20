package com.becool.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NewClient {
    private UUID id;
    private String name;
    private String email;
    private String password;
}
