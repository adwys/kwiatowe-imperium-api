package com.kwiatowe_imperium.api.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    private Long quantity;

    @OneToOne(cascade=CascadeType.ALL)
    private Product product;

    @ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Cart cart;


//    @Transient
//    public Double getTotalPrice() {
//        return getProduct().getPrice().toBigInteger().doubleValue() * getQuantity();
//    }

}
