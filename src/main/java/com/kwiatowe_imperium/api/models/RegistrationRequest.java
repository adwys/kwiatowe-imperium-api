package com.kwiatowe_imperium.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegistrationRequest {

    private final String name;
    private final String surname;
    private final String email;
    private final String username;
    private final String password;
}
