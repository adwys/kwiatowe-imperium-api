package com.kwiatowe_imperium.api.models;


import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

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

    public void updateFrom(final Product source) {
        name = source.name;
        description = source.description;
        price = source.price;
    }
}
