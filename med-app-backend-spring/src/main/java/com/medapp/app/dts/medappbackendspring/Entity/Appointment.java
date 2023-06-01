package com.medapp.app.dts.medappbackendspring.Entity;

import com.medapp.app.dts.medappbackendspring.Enum.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @ManyToOne
    private User doctor;
    @ManyToOne
    private User client;
    @ManyToOne
    private Ad ad;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime appointmentTime;
    private Long desiredPrice;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

}
