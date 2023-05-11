package com.medapp.app.dts.medappbackendspring.Dto;

import com.medapp.app.dts.medappbackendspring.Entity.Category;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdDto {
    private String title;
    private String description;
    private Long price;
    private String address;
    private String city;
    private Double rating;
    private User user;
    private Category category;
}
