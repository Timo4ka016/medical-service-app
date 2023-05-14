package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Dto.*;
import com.medapp.app.dts.medappbackendspring.Entity.*;
import com.medapp.app.dts.medappbackendspring.Repository.*;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private CityRepository cityRepository;

    private final PasswordEncoder passwordEncoder;
    private static final ModelMapper mapper = new ModelMapper();

    /*
     *  Начало сервиса профиля
     * */

    public DoctorMainInfo profileForDoctor(User user) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Доктор не найден"));
        return mapper.map(myUser, DoctorMainInfo.class);
    }

    public void updateDoctor(User user, UpdateDoctorDto request) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(null);
        if (request.getFirstname() != null && !request.getFirstname().trim().isEmpty()) {
            existingUser.setFirstname(request.getFirstname().trim());
        }
        if (request.getLastname() != null && !request.getLastname().trim().isEmpty()) {
            existingUser.setLastname(request.getLastname().trim());
        }
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            existingUser.setDescription(request.getDescription());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
            existingUser.setPhoneNumber(request.getPhoneNumber().trim());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        }
        if (request.getCityId() != null) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new NotFoundException("Город не найден"));
            existingUser.setCity(city);
            List<Ad> userAds = existingUser.getAds();
            for (Ad ad : userAds) {
                ad.setCity(city.getCity_name());
            }
            adRepository.saveAll(userAds);
        }
        userRepository.save(existingUser);
    }

    /*
     *  Конец сервиса профиля
     * */

    /*
     *  Начало сервиса категорий
     * */

    public void addCategoryToDoctor(User user, List<Long> categoryIds) {
        User myUser = userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));
        Set<Category> categoriesToAdd = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
            categoriesToAdd.add(category);
        }

        myUser.getCategories().addAll(categoriesToAdd);
        userRepository.save(user);
    }

    public void removeCategoryFromDoctor(User user, List<Long> categoryIds) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Set<Category> categoriesToRemove = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
            categoriesToRemove.add(category);
        }

        myUser.getCategories().removeAll(categoriesToRemove);
        userRepository.save(myUser);
    }

    /*
     *  Конец сервиса категорий
     * */

    /*
     *  Начало сервиса объявлений
     * */

    public void createAd(User user, Long categoryId, CreateAdDto createAdDto) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Category myCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        if (createAdDto.getTitle() == null || createAdDto.getTitle().trim().isEmpty() ||
                createAdDto.getDescription() == null || createAdDto.getDescription().trim().isEmpty() ||
                createAdDto.getPrice() == null || createAdDto.getAddress() == null ||
                createAdDto.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Не заполнены все обязательные поля");
        }
        Ad ad = Ad.builder()
                .title(createAdDto.getTitle().trim())
                .description(createAdDto.getDescription().trim())
                .price(createAdDto.getPrice())
                .address(createAdDto.getAddress())
                .city(myUser.getCity().getCity_name())
                .rating(myUser.getRating())
                .user(myUser)
                .category(myCategory)
                .build();
        adRepository.save(ad);
    }

    public void updateAd(User user, Long adId, Long categoryId, UpdateAdDto updateAdDto) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        if (!myUser.getAds().contains(ad)) {
            throw new AccessDeniedException("Вы не можете редактировать это объявление");
        }
        ad.setTitle(StringUtils.isNotBlank(updateAdDto.getTitle()) ? updateAdDto.getTitle().trim() : ad.getTitle());
        ad.setDescription(StringUtils.isNotBlank(updateAdDto.getDescription()) ? updateAdDto.getDescription() : ad.getDescription());
        ad.setPrice(updateAdDto.getPrice() != null ? updateAdDto.getPrice() : ad.getPrice());
        ad.setAddress(StringUtils.isNotBlank(updateAdDto.getAddress()) ? updateAdDto.getAddress().trim() : ad.getAddress());
        ad.setCategory(category);
        adRepository.save(ad);
    }

    public void deleteAd(User user, Long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new NotFoundException("Ad not found"));
        if (!ad.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this ad");
        }
        adRepository.delete(ad);
    }

    public List<GetDoctorAds> doctorAds(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Доктор не найден."))
                .getAds()
                .stream()
                .map(ad -> {
                    GetDoctorAds requestBody = mapper.map(ad, GetDoctorAds.class);
                    requestBody.setCategory(ad.getCategory().getName());
                    DoctorMainInfo doctor = mapper.map(ad.getUser(), DoctorMainInfo.class);
                    requestBody.setDoctor(doctor);
                    return requestBody;
                })
                .collect(Collectors.toList());
    }

    public GetDoctorAdById getDoctorAdById(User user, Long adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Объявление не найдено"));
        GetDoctorAdById adDto = mapper.map(ad, GetDoctorAdById.class);
        adDto.setCategory(ad.getCategory().getName());
        DoctorMainInfo doctor = mapper.map(ad.getUser(), DoctorMainInfo.class);
        adDto.setDoctor(doctor);
        return adDto;
    }

    /*
     *  Конец сервиса объявлений
     * */

    /*
     *  Начало сервиса отзывов
     * */

    public List<Feedback> getMyFeedback(User user, Double rating) {
        Specification<Feedback> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("doctor"), user));
        if (rating != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("rating"), rating));
        }
        return feedbackRepository.findAll(spec);
    }

    /*
     *  Конец сервиса отзывов
     * */

    /*
     *  Начало сервиса города
     * */

    public void addCityToDoctor(User user, Long cityId) {
        User myUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Доктор не найден"));
        City myCity = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("Город не найден"));
        myUser.setCity(myCity);
        userRepository.save(myUser);
    }

    /*
     *  Конец сервиса города
     * */

}
