package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDoctorDto {
    private String firstname;
    private String lastname;
    private String description;
    private String  phoneNumber;
    private String password;
    private Long cityId;

}
