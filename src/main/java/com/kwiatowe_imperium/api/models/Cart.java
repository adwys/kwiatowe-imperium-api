package com.kwiatowe_imperium.api.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.mapping.Map;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Cart implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;
    private boolean ordered = false;
    @OneToMany(mappedBy = "cart",fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<OrderItem> products = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    private UserModel userModel;

}
