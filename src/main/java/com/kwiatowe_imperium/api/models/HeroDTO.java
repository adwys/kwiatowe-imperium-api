package com.kwiatowe_imperium.api.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeroDTO {

    private Long id;
    private String title;
    private String subtitle;
    private String buttonText;
    private Image image;
    private Category category;
}
