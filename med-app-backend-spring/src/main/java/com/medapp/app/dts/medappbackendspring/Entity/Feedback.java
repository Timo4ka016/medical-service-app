package com.medapp.app.dts.medappbackendspring.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Double rating;
    @JsonIgnore
    @ManyToOne
    private User client;
    @JsonIgnore
    @ManyToOne
    private User doctor;
    
}
