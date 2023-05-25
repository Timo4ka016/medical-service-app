package com.medapp.app.dts.medappbackendspring.Dto;

import com.medapp.app.dts.medappbackendspring.Entity.Category;
import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfileInfoForClient {
    private Long id;
    private String firstname;
    private String lastname;
    private String city;
    private String email;
    private String description;
    private Long phoneNumber;
    private List<FeedbackDto> receivedFeedbacks;

}
