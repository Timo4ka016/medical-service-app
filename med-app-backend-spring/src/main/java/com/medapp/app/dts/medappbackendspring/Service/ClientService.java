package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Dto.*;
import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import com.medapp.app.dts.medappbackendspring.Entity.NotFoundException;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Enum.Role;
import com.medapp.app.dts.medappbackendspring.Repository.AdRepository;
import com.medapp.app.dts.medappbackendspring.Repository.FeedbackRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private static final ModelMapper mapper = new ModelMapper();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    private final PasswordEncoder passwordEncoder;


    /*
     *  Начало сервиса профиля для клиента
     * */

    public ProfileInfoForClient profileForClient(User user) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Клиент не найден"));
        return mapper.map(myUser, ProfileInfoForClient.class);
    }

    public void updateClient(User user, UpdateClientDto request) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(null);
        if (request.getFirstname() != null && !request.getFirstname().trim().isEmpty()) {
            existingUser.setFirstname(request.getFirstname().trim());
        }
        if (request.getLastname() != null && !request.getLastname().trim().isEmpty()) {
            existingUser.setLastname(request.getLastname().trim());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
            existingUser.setPhoneNumber(request.getPhoneNumber().trim());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        }
        userRepository.save(existingUser);
    }

    /*
     *  Конец сервиса профиля для клиента
     * */

    /*
     *  Начало сервиса взаимодействия с доктором для клиента
     * */

    public List<SearchDoctorResponse> searchDoctors(Long id, String firstname, String lastname) {
        Specification<User> spec = Specification.where(null);
        if (id != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        if (firstname != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstname"), firstname));
        if (lastname != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastname"), lastname));

        spec = spec.and((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("role"), Role.USER_DOCTOR),
                    criteriaBuilder.notEqual(root.get("role"), Role.USER_CLIENT),
                    criteriaBuilder.notEqual(root.get("role"), Role.USER_ADMIN)
            );
        });

        List<User> users = userRepository.findAll(spec);
        return users.stream().map(user -> mapper.map(user, SearchDoctorResponse.class)).collect(Collectors.toList());
    }

    public DoctorProfileInfoForClient doctorProfileInfoForUser(Long doctorId) {
        User searchedDoctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Доктор не найден"));

        return mapper.map(searchedDoctor, DoctorProfileInfoForClient.class);
    }

    /*
     *  Конец сервиса взаимодействия с доктором для клиента
     * */

    /*
     *  Начало сервиса отзывов для клиента
     * */

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
                .orElseThrow(() -> new NotFoundException("Отзыв не найден"));
        if (!myUser.getGivenFeedbacks().contains(updatedFeedback)) {
            throw new AccessDeniedException("Вы не можете редактировать этот отзыв");
        }
        updatedFeedback.setText(StringUtils.isNotBlank(feedback.getText()) ? feedback.getText().trim() : updatedFeedback.getText());
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

    /*
     *  Конец сервиса отзывов для клиента
     * */

}
