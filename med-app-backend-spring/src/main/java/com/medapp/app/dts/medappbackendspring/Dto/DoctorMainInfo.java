package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorMainInfo {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private Double rating;
    private String city;



}
