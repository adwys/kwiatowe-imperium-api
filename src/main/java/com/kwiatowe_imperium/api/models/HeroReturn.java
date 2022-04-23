package com.kwiatowe_imperium.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeroReturn {

    private Long id;
    private boolean main;
    private String titlePl;
    private String subtitlePl;
    private String buttonTextPl;
    private String titleEn;
    private String subtitleEn;
    private String buttonTextEn;
    private Image image;
    private CategoryHero category;
}
