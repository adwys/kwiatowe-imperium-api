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

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderItem> products = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    private UserModel userModel;

//    @Transient
//    public Double getTotalOrderPrice() {
//        double sum = 0D;
//        List<OrderItem> orderProducts = getProducts();
//        for (OrderItem p : orderProducts) {
//            sum += p.getTotalPrice();
//        }
//        return sum;
//    }


}
