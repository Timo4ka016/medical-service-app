package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDoctorAdById {
    private Long id;
    private String title;
    private String category;
    private String address;
    private String description;
    private String city;
    private Long price;
    private String rating;
    private DoctorMainInfo doctor;
}
