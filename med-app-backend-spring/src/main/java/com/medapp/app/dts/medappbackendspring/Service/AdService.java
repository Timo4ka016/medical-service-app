package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Dto.CreateAdDto;
import com.medapp.app.dts.medappbackendspring.Dto.UpdateAdDto;
import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import com.medapp.app.dts.medappbackendspring.Entity.NotFoundException;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Repository.AdRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdService {
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private UserRepository userRepository;

    public void createAd(User user, CreateAdDto createAdDto) {
        User myUser = userRepository.findById(user.getId()).orElseThrow(null);
        Ad ad = Ad.builder()
                .title(createAdDto.getTitle())
                .description(createAdDto.getDescription())
                .price(createAdDto.getPrice())
                .address(createAdDto.getAddress())
                .user(myUser)
                .build();
        adRepository.save(ad);
    }

    public void updateAd(User user, Long adId, UpdateAdDto updateAdDto) {
        User myUser = userRepository.findById(user.getId()).orElseThrow(null);
        Optional<Ad> optionalAd = adRepository.findById(adId);
        if (optionalAd.isPresent()) {
            Ad ad = optionalAd.get();
            if (myUser.getAds().contains(ad)) {
                if (updateAdDto.getTitle() != null && !updateAdDto.getTitle().trim().isEmpty()) {
                    ad.setTitle(updateAdDto.getTitle().trim());
                }
                if (updateAdDto.getDescription() != null && !updateAdDto.getDescription().trim().isEmpty()) {
                    ad.setDescription(updateAdDto.getDescription());
                }
                if (updateAdDto.getPrice() != null) {
                    ad.setPrice(updateAdDto.getPrice());
                }
                if (updateAdDto.getAddress() != null && !updateAdDto.getAddress().trim().isEmpty()) {
                    ad.setAddress(updateAdDto.getAddress());
                }
                adRepository.save(ad);
            } else {
                throw new AccessDeniedException("Вы не можете редактировать это объявление");
            }
        } else {
            throw new NotFoundException("Объявление не найдено");
        }
    }

    public void deleteAd(User user, Long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new NotFoundException("Ad not found"));
        if (!ad.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this ad");
        }
        adRepository.delete(ad);
    }


}
