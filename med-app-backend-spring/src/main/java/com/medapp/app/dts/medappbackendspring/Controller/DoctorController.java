package com.medapp.app.dts.medappbackendspring.Controller;

import com.medapp.app.dts.medappbackendspring.Dto.*;
import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
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
    private final AuthService authService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AdService adService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FeedbackService feedbackService;


    @GetMapping("/profile")
    public ResponseEntity<ProfileInfoForDoctor> profile(
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

    @PostMapping("/ads/ad/create")
    public void createAd(
            @AuthenticationPrincipal User user,
            @RequestParam Long categoryId,
            @RequestBody CreateAdDto createAdDto
    ) {
        adService.createAd(user, categoryId, createAdDto);
    }

    @PutMapping("/ads/ad/update")
    public void updateAd(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId,
            @RequestParam Long categoryId,
            @RequestBody UpdateAdDto updateAdDto
    ) {
        adService.updateAd(user, adId, categoryId, updateAdDto);
    }

    @GetMapping("/ads")
    public ResponseEntity<List<AdDto>> getDoctorAds(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(adService.doctorAds(user));
    }

    @DeleteMapping("/ads/ad/delete")
    public void deleteAd(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId
    ) {
        adService.deleteAd(user, adId);
    }

    @PutMapping("/category/add")
    public void addCategoryToDoctor(
            @AuthenticationPrincipal User user,
            @RequestBody List<Long> categoryIds) {
        categoryService.addCategoryToDoctor(user, categoryIds);
    }

    @PutMapping("/category/remove")
    public void removeCategoryToDoctor(
            @AuthenticationPrincipal User user,
            @RequestBody List<Long> categoryIds) {
        categoryService.removeCategoryFromDoctor(user, categoryIds);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<Feedback>> getMyFeedbacks(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Double rating
    ) {
        return ResponseEntity.ok(feedbackService.getMyFeedback(user, rating));
    }

}
