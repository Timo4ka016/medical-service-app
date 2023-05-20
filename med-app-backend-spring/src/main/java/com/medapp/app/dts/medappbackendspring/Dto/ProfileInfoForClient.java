package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoForClient {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;

}
