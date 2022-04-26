package com.kwiatowe_imperium.api.models;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.ManyToMany;
import java.util.List;

@Data
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;
    private boolean isVisible;
//    public List<ProductDTO> products;


}
