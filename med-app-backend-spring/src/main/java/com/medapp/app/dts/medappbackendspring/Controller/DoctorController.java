package com.medapp.app.dts.medappbackendspring.Controller;

import com.medapp.app.dts.medappbackendspring.Dto.CreateAdDto;
import com.medapp.app.dts.medappbackendspring.Dto.DoctorDto;
import com.medapp.app.dts.medappbackendspring.Dto.UpdateAdDto;
import com.medapp.app.dts.medappbackendspring.Dto.UpdateDoctorDto;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import com.medapp.app.dts.medappbackendspring.Service.AdService;
import com.medapp.app.dts.medappbackendspring.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AdService adService;

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('USER_DOCTOR')")
    public void updateDoctor(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateDoctorDto request
    ) {
        doctorService.updateDoctor(user, request);
    }

    @PostMapping("/create-ad")
    @PreAuthorize("hasAuthority('USER_DOCTOR')")
    public void createAd(
            @AuthenticationPrincipal User user,
            @RequestBody CreateAdDto createAdDto
    ) {
        adService.createAd(user, createAdDto);
    }

    @PutMapping("/update-ad")
    @PreAuthorize("hasAuthority('USER_DOCTOR')")
    public void updateAd(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId,
            @RequestBody UpdateAdDto updateAdDto
    ) {
        adService.updateAd(user, adId, updateAdDto);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('USER_DOCTOR')")
    public void deleteAd(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId
    ) {
        adService.deleteAd(user, adId);
    }

    @GetMapping("/get-doctor")
    public ResponseEntity<List<DoctorDto>> getDoctorInfo(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname
    ) {
        return ResponseEntity.ok(doctorService.getDoctorInfo(id, firstname, lastname));
    }


}
