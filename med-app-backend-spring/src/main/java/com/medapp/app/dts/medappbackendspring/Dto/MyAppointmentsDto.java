package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyAppointmentsDto {
    private Long id;
    private String message;
    private FullNameUser client;
    private FullNameUser doctor;
    private AdInfo ad;
    private LocalDateTime appointmentTime;
    private Long desiredPrice;
    private String status;
}
