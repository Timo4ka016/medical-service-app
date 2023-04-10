package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import com.medapp.app.dts.medappbackendspring.Entity.NotFoundException;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Repository.AdRepository;
import com.medapp.app.dts.medappbackendspring.Repository.FeedbackRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    public void addFeedback(User user, Long adId, Feedback feedback) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Обьявление не найдено"));
        User doctor = ad.getUser();
        Feedback myFeedback = Feedback.builder()
                .text(feedback.getText())
                .rating(feedback.getRating())
                .client(user)
                .doctor(doctor)
                .build();
        doctor.getReceivedFeedbacks().add(myFeedback);
        feedbackRepository.save(myFeedback);
        userRepository.save(doctor);
    }


}
