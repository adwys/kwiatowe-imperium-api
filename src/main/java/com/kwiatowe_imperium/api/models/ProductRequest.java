package com.kwiatowe_imperium.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String nameEn;
    private String namePl;
    private String descriptionEn;
    private String descriptionPl;
    private BigDecimal price;
    private List<Long> images;
    private List<Long> categories;

}
