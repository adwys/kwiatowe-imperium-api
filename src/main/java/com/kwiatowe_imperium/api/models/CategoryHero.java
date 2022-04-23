package com.kwiatowe_imperium.api.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryHero {

    private Long id;
    private String name;
    private boolean is_visible;

}
