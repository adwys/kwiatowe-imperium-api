package com.kwiatowe_imperium.api.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
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
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @NonNull
    private String name;
    @Nullable
    private String description;
    @NonNull
    private BigDecimal price;
    @OneToMany(mappedBy = "product")
    public Set<Image> images;


    public void updateFrom(final Product source) {
        name = source.name;
        description = source.description;
        price = source.price;
        images = source.images;
    }
}
