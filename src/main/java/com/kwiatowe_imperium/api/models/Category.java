package com.kwiatowe_imperium.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

    private boolean isVisible;


    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    public List<Product> products;

    public void updateFrom(final Category source) {
        id = source.id;
        nameEn = source.nameEn;
        namePl = source.namePl;
        isVisible = source.isVisible;
        products = source.products;
    }

}
