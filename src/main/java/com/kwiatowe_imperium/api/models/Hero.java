package com.kwiatowe_imperium.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
//@RequiredArgsConstructor
public class Hero implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;
    private boolean main;

    private String titlePl;
    private String subtitlePl;
    private String buttonTextPl;

    private String titleEn;
    private String subtitleEn;
    private String buttonTextEn;

    @OneToOne
    private Category category;

    @OneToOne
    private Image image;

    public void updateFrom(final Hero source) {
        titlePl = source.titlePl;
        titleEn = source.titleEn;
        subtitleEn = source.subtitleEn;
        subtitlePl = source.subtitlePl;
        buttonTextEn = source.buttonTextEn;
        buttonTextPl = source.buttonTextPl;
    }

}
