package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoForDoctor {
    private Long id;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private Double rating;
}
