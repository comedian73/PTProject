package com.becool.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateClient {
    private String name;
    private String email;
    private String phone;
}
