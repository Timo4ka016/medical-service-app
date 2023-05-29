package com.medapp.app.dts.medappbackendspring.Dto;

import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long id;
    private String text;
    private Double rating;
    private FullNameClient client;
    private Long doctorId;
}
