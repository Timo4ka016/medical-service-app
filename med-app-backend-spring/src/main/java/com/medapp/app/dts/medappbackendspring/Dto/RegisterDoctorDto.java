package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDoctorDto {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneNumber;

}
