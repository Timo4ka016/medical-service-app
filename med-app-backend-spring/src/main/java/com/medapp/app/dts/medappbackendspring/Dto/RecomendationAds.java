package com.medapp.app.dts.medappbackendspring.Dto;

import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecomendationAds {
    private String title;
    private String address;
    private String city;
    private String price;
}