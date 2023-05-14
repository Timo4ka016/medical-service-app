package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetDoctorAds {
    private Long id;
    private String title;
    private String category;
    private String address;
    private Long price;
    private String rating;
    private DoctorMainInfo doctor;
}
