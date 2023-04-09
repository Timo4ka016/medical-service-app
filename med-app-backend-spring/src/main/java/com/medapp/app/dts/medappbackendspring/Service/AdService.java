package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Dto.AdDto;
import com.medapp.app.dts.medappbackendspring.Dto.CreateAdDto;
import com.medapp.app.dts.medappbackendspring.Dto.UpdateAdDto;
import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import com.medapp.app.dts.medappbackendspring.Entity.Category;
import com.medapp.app.dts.medappbackendspring.Entity.NotFoundException;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Repository.AdRepository;
import com.medapp.app.dts.medappbackendspring.Repository.CategoryRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdService {
    private static final ModelMapper mapper = new ModelMapper();
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

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

    public List<AdDto> doctorAds(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Доктор не найден.")).getAds()
                .stream().map(ad -> mapper.map(ad, AdDto.class)).collect(Collectors.toList());
    }


}
