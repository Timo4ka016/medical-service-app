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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        double newRating = sum / doctor.getReceivedFeedbacks().size();
        doctor.setRating(newRating);
        userRepository.save(doctor);

        List<Ad> doctorAds = adRepository.findByUserId(doctor.getId());
        for (Ad adItem : doctorAds) {
            adItem.setRating(newRating);
        }
        adRepository.saveAll(doctorAds);

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

        List<Ad> doctorAds = adRepository.findByUserId(doctor.getId());
        for (Ad ad : doctorAds) {
            ad.setRating(newRating);
        }
        adRepository.saveAll(doctorAds);


    }

    public List<FeedbackDto> getMyFeedbacks(User user) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        List<Feedback> myFeedbacks = myUser.getGivenFeedbacks();
        return myFeedbacks.stream().map(feedback -> mapper.map(feedback, FeedbackDto.class)).collect(Collectors.toList());
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

        List<Ad> doctorAds = adRepository.findByUserId(doctor.getId());
        for (Ad ad : doctorAds) {
            ad.setRating(newRating);
        }
        adRepository.saveAll(doctorAds);

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


    public List<GetDoctorAds> getRecomendationAds(@AuthenticationPrincipal User user) {
        User currentUser = userRepository.findById(user.getId()).orElseThrow();
        String city = currentUser.getCity().getCity_name();
        Double minRating = 3.0;
        Double maxRating = 5.0;

        List<Ad> ads = adRepository.findByCityAndRatingBetween(city, minRating, maxRating);
        return ads.stream()
                .map(ad -> {
                    GetDoctorAds doctorAd = mapper.map(ad, GetDoctorAds.class);
                    DoctorMainInfo doctorInfo = mapper.map(ad.getUser(), DoctorMainInfo.class);
                    doctorAd.setDoctor(doctorInfo);
                    doctorAd.setCategory(ad.getCategory().getName());
                    return doctorAd;
                })
                .collect(Collectors.toList());
    }

    public GetDoctorAdById getAdById(User user, Long adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        GetDoctorAdById adDto = mapper.map(ad, GetDoctorAdById.class);
        adDto.setCategory(ad.getCategory().getName());
        DoctorMainInfo doctor = mapper.map(ad.getUser(), DoctorMainInfo.class);
        adDto.setDoctor(doctor);
        return adDto;
    }

    public List<GetDoctorAds> getAdByCategory(User user, Long categoryId) {
        List<Ad> ads = adRepository.findByCategoryId(categoryId);
        return  ads.stream().map(ad -> {
            GetDoctorAds doctorAd = mapper.map(ad, GetDoctorAds.class);
            DoctorMainInfo doctorInfo = mapper.map(ad.getUser(), DoctorMainInfo.class);
            doctorAd.setDoctor(doctorInfo);
            doctorAd.setCategory(ad.getCategory().getName());
            return doctorAd;
        })
                .collect(Collectors.toList());
    }

    /*
     *  Конец сервиса объявлений
     * */


}
