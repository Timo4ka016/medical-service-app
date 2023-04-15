package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import com.medapp.app.dts.medappbackendspring.Entity.NotFoundException;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Repository.AdRepository;
import com.medapp.app.dts.medappbackendspring.Repository.FeedbackRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

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

        double sum = 0;
        for (Feedback f : doctor.getReceivedFeedbacks()) {
            sum += f.getRating();
        }
        double rating = sum / doctor.getReceivedFeedbacks().size();
        doctor.setRating(rating);
        userRepository.save(doctor);
    }

    public void updateFeedback(User user, Long feedbackId, Feedback feedback) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Feedback updatedFeedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(()-> new NotFoundException("Отзыв не найден"));
        if (!myUser.getGivenFeedbacks().contains(updatedFeedback)) {
            throw new AccessDeniedException("Вы не можете редактировать этот отзыв");
        }
        updatedFeedback.setText(StringUtils.isNotBlank(feedback.getText()) ? feedback.getText().trim() : updatedFeedback.getText() );
        updatedFeedback.setRating(feedback.getRating() != null ? feedback.getRating() : updatedFeedback.getRating());
        feedbackRepository.save(updatedFeedback);

        User doctor = updatedFeedback.getDoctor();
        double sum = 0;
        for (Feedback f : doctor.getReceivedFeedbacks()) {
            sum += f.getRating();
        }
        double newRating = sum / doctor.getReceivedFeedbacks().size();
        doctor.setRating(newRating);
        userRepository.save(doctor);

    }

    public void deleteFeedback(User user, Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundException("Отзыв не найден"));
        if (!feedback.getClient().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this ad");
        }
        feedbackRepository.delete(feedback);

        User doctor = feedback.getDoctor();
        double sum = 0;
        int count = 0;
        for (Feedback f : doctor.getReceivedFeedbacks()) {
            if (!f.getId().equals(feedbackId)) {
                sum += f.getRating();
                count++;
            }
        }
        double newRating = count > 0 ? sum / count : 0;
        doctor.setRating(newRating);
        userRepository.save(doctor);
    }

    public List<Feedback> getMyFeedback(User user, Double rating) {
        Specification<Feedback> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("doctor"), user));
        if (rating != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("rating"), rating));
        }
        return feedbackRepository.findAll(spec);
    }


}
