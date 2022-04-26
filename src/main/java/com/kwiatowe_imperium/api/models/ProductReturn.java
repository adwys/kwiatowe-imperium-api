package com.kwiatowe_imperium.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReturn {

    private String nameEn;
    private String namePl;
    private String descriptionEn;
    private String descriptionPl;
    private BigDecimal price;
    private List<Image> images;
    private List<CategoryDTO> categories;
}
