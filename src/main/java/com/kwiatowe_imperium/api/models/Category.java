package com.kwiatowe_imperium.api.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String namePl;
    @NotNull
    private String nameEn;

    private Boolean isVisible;

    @ManyToMany(mappedBy = "categories")
    public List<Product> products;

    public void updateFrom(final Category source) {
        id = source.id;
        nameEn = source.nameEn;
        namePl = source.namePl;
        isVisible = source.isVisible;
        products = source.products;
    }

}
