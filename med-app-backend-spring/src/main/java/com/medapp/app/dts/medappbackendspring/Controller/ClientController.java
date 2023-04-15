package com.medapp.app.dts.medappbackendspring.Controller;

import com.medapp.app.dts.medappbackendspring.Dto.ProfileInfoForUser;
import com.medapp.app.dts.medappbackendspring.Dto.SearchDoctorResponse;
import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Service.DoctorService;
import com.medapp.app.dts.medappbackendspring.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasAuthority('USER_CLIENT')")
public class ClientController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/feedback/create")
    public void addFeedback(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId,
            @RequestBody Feedback feedback
    ) {
        feedbackService.addFeedback(user, adId, feedback);
    }

    @PutMapping("/feedback/update")
    public void updateFeedback(
            @AuthenticationPrincipal User user,
            @RequestParam Long feedbackId,
            @RequestBody Feedback feedback
    ) {
        feedbackService.updateFeedback(user, feedbackId, feedback);
    }

    @DeleteMapping("/feedback/delete")
    public void deleteFeedback(
            @AuthenticationPrincipal User user,
            @RequestParam Long feedbackId
    ) {
        feedbackService.deleteFeedback(user, feedbackId);
    }

    @GetMapping("/search-doctors")
    public ResponseEntity<List<SearchDoctorResponse>> searchDoctors(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname
    ) {
        return ResponseEntity.ok(doctorService.searchDoctors    (id, firstname, lastname));
    }

    @GetMapping("/selected-doctor")
    public ResponseEntity<ProfileInfoForUser> selectedDoctorInfo(
            @RequestParam Long doctorId
    ) {
        return ResponseEntity.ok(doctorService.profileForUser(doctorId));
    }

}
