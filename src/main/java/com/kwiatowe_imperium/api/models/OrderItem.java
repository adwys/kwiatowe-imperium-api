package com.kwiatowe_imperium.api.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kwiatowe_imperium.api.servies.ProductService;
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

    @OneToOne(cascade=CascadeType.DETACH)
    private Product product;

    @ManyToOne(cascade=CascadeType.DETACH,fetch = FetchType.EAGER)
    @JsonIgnore
    private Cart cart;

    public ProductDTO getDTO(String lang){
        if(lang.equals("en")){
            return ProductService.MapToEng(product);
        }
        else{
            return ProductService.MapToPl(product);
        }

    }

//    @Transient
//    public Double getTotalPrice() {
//        return getProduct().getPrice().toBigInteger().doubleValue() * getQuantity();
//    }

}
