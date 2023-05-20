package com.medapp.app.dts.medappbackendspring.Controller;

import com.medapp.app.dts.medappbackendspring.Dto.*;
import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER_DOCTOR')")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;


    @GetMapping("/profile")
    public ResponseEntity<DoctorMainInfo> profile(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(doctorService.profileForDoctor(user));
    }

    @PutMapping("/profile/update")
    public void updateDoctor(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateDoctorDto request
    ) {
        doctorService.updateDoctor(user, request);
    }

        @PutMapping("/category/add")
        public void addCategoryToDoctor(
                @AuthenticationPrincipal User user,
                @RequestBody List<Long> categoryIds) {
            doctorService.addCategoryToDoctor(user, categoryIds);
        }

    @PutMapping("/category/remove")
    public void removeCategoryToDoctor(
            @AuthenticationPrincipal User user,
            @RequestBody List<Long> categoryIds) {
        doctorService.removeCategoryFromDoctor(user, categoryIds);
    }

    @PostMapping("/ads/ad/create")
    public void createAd(
            @AuthenticationPrincipal User user,
            @RequestParam Long categoryId,
            @RequestBody CreateAdDto createAdDto
    ) {
        doctorService.createAd(user, categoryId, createAdDto);
    }

    @PutMapping("/ads/ad/update")
    public void updateAd(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId,
            @RequestParam Long categoryId,
            @RequestBody UpdateAdDto updateAdDto
    ) {
        doctorService.updateAd(user, adId, categoryId, updateAdDto);
    }

    @GetMapping("/ads")
    public ResponseEntity<List<GetDoctorAds>> getDoctorAds(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(doctorService.doctorAds(user));
    }

    @GetMapping("/ads/ad")
    public ResponseEntity<GetDoctorAdById> getDoctorAdById(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId
    ) {
        return ResponseEntity.ok(doctorService.getDoctorAdById(user, adId));
    }

    @DeleteMapping("/ads/ad/delete")
    public void deleteAd(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId
    ) {
        doctorService.deleteAd(user, adId);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<Feedback>> getMyFeedbacks(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Double rating
    ) {
        return ResponseEntity.ok(doctorService.getMyFeedback(user, rating));
    }

    @PutMapping("/city/add")
    public void addCityToDoctor(
            @AuthenticationPrincipal User user,
            @RequestParam Long cityId
    ) {
        doctorService.addCityToDoctor(user, cityId);
    }

}
