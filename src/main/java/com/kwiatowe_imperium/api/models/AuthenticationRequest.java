package com.kwiatowe_imperium.api.models;

import lombok.Data;

public @Data
class AuthenticationRequest {

    private String email;
    private String password;

}