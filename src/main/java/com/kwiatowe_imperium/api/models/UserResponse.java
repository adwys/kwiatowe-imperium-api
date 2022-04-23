package com.kwiatowe_imperium.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String surname;
    private String address;
    private String postalCode;
    private String City;
    private String email;

    public Set<Role> roles = new HashSet<>();

    private Cart cart;
}
