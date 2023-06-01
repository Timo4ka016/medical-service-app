package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAppointmentDto {
    private String message;
    private LocalDateTime appointmentTime;
    private Long desiredPrice;
}
