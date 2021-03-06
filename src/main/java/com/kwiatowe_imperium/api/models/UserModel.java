package com.kwiatowe_imperium.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@RequiredArgsConstructor
public class UserModel {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String surname;

    private String address;
    private String postalCode;
    private String city;

    @NonNull
    private String email;
    @NonNull
    @JsonIgnore
    private String password;



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public Set<Role> roles = new HashSet<>();

    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnore
    private Cart cart;


    public void updateForm(final UserModel source){
        name = source.name;
        address = source.address;
        if(address == null)address = "";
        postalCode = source.postalCode;
        if(postalCode == null)postalCode = "";
        city = source.city;
        if(city == null)city = "";
        surname = source.surname;
    }

}
