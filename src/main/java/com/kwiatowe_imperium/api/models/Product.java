package com.kwiatowe_imperium.api.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@RequiredArgsConstructor
public class Product implements Serializable {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "user_sequence"
    )
    private Long id;

    @NonNull
    private String nameEn;

    @NonNull
    private String namePl;

    @Nullable
    private String descriptionEn;
    @Nullable
    private String descriptionPl;

    @NonNull
    private BigDecimal price;

    @OneToMany(mappedBy = "product")
    public List<Image> images;

    @ManyToMany
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    public List<Category> categories;


    public void updateFrom(final Product source) {

        nameEn = source.nameEn;
        if(nameEn == null)nameEn = "";
        if(source.namePl != null)namePl = source.namePl;
        descriptionEn = source.descriptionEn;
        if(descriptionEn == null)descriptionEn = "";
        descriptionPl = source.descriptionPl;
        if(descriptionPl == null)descriptionPl = "";
        price = source.price;
        images = source.images;
    }
}
