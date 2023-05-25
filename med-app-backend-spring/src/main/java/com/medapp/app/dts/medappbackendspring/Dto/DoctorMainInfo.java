package com.medapp.app.dts.medappbackendspring.Dto;

import lombok.*;

import java.util.List;

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
    private String description;
    private Double rating;
    private String city;
    private List<FeedbackDto> receivedFeedbacks;

}
