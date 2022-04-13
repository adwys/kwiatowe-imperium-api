package com.kwiatowe_imperium.api.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class HeroRequest {

    private Long id;
    private String titlePl;
    private String subtitlePl;
    private String buttonTextPl;
    private String titleEn;
    private String subtitleEn;
    private String buttonTextEn;
    private Long imageId;
    private Long categoryId;
}
