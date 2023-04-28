package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Dto.*;
import com.medapp.app.dts.medappbackendspring.Entity.*;
import com.medapp.app.dts.medappbackendspring.Enum.Role;
import com.medapp.app.dts.medappbackendspring.Repository.AdRepository;
import com.medapp.app.dts.medappbackendspring.Repository.CityRepository;
import com.medapp.app.dts.medappbackendspring.Repository.FeedbackRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    @Autowired
    private CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;


    /*
     *  Начало сервиса профиля
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
     *  Конец сервиса профиля
     * */

    /*
     *  Начало сервиса взаимодействия с доктором
     * */

    public List<SearchDoctorResponse> searchDoctors(Long id, String firstname, String lastname, String category, Double rating) {
        Specification<User> spec = Specification.where(null);
        if (id != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        if (firstname != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstname"), firstname));
        if (lastname != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastname"), lastname));
        if (category != null)
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<User, Category> userCategoryJoin = root.join("categories");
                return userCategoryJoin.get("name").in(category);
            });
        if (rating != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("rating"), rating));


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
     *  Конец сервиса взаимодействия с доктором
     * */

    /*
     *  Начало сервиса отзывов
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
     *  Конец сервиса отзывов
     * */

    /*
     *  Начало сервиса города
     * */

    public void addCityToClient(User user, Long cityId) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Клиент не найден"));
        City myCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("Город не найден"));
        myUser.setCity(myCity);
        userRepository.save(myUser);
    }

    /*
     *  Конец сервиса города
     * */

    /*
     *  Начало сервиса рекомендаций
     * */

    public List<RecomendationAds> getRecomendationAds(User client, int limit) {
        City clientCity = client.getCity();

        // Получаем список докторов, имеющих объявления
        List<User> doctorsWithAds = userRepository.findAllByRoleAndAdsIsNotNull(Role.USER_DOCTOR);

        // Фильтруем объявления по городу клиента и городу доктора, если они различаются
        List<RecomendationAds> ads = doctorsWithAds.stream()
                .filter(doctor -> !Objects.equals(doctor.getCity(), clientCity))
                .flatMap(doctor -> doctor.getAds().stream()
                        .filter(ad -> Objects.equals(ad.getUser().getCity(), clientCity))
                        .map(ad -> {
                            RecomendationAds recommendation = new RecomendationAds();
                            recommendation.setAd(ad);
                            recommendation.setDoctor(doctor);
                            return recommendation;
                        }))
                .collect(Collectors.toList());

        // Фильтруем рекомендации по рейтингу доктора
        ads = ads.stream()
                .filter(ad -> ad.getDoctor().getRating() >= 3.6)
                .collect(Collectors.toList());

        // Сортируем рекомендации по убыванию рейтинга доктора и выбираем первые `limit` рекомендаций
        ads.sort((ad1, ad2) -> Double.compare(ad2.getDoctor().getRating(), ad1.getDoctor().getRating()));
        ads = ads.subList(0, Math.min(limit, ads.size()));

        return ads.stream().map(ad -> mapper.map(ad, RecomendationAds.class)).collect(Collectors.toList());
    }
    /*
     *  Конец сервиса рекомендаций
     * */


}
