package com.medapp.app.dts.medappbackendspring.Dto;

import com.medapp.app.dts.medappbackendspring.Entity.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdDto {
    private Long id;
    private String title;
    private String description;
    private Long price;
    private Double rating;
    private String address;
    private String category;

}
