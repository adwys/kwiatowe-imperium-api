package com.kwiatowe_imperium.api.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
//@RequiredArgsConstructor
public class Category implements Serializable {

    @Id

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @NotNull
    private String name;

    private Boolean is_visible;

    @ManyToMany(mappedBy = "categories")
    public List<Product> products;

    public void updateFrom(final Category source) {
        id = source.id;
        name = source.name;
        is_visible = source.is_visible;
        products = source.products;
    }

}
