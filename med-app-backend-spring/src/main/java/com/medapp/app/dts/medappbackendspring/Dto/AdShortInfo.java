package com.medapp.app.dts.medappbackendspring.Dto;

import com.medapp.app.dts.medappbackendspring.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdShortInfo {
    private Long id;
    private String title;
    private String address;
    private Category category;
    private Double rating;
    private Long price;
    private FullNameUser doctor;
}
