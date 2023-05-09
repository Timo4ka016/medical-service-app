package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClientDto {
    private String firstname;
    private String lastname;
    private String password;

}
