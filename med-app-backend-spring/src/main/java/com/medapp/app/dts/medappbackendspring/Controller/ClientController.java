package com.medapp.app.dts.medappbackendspring.Controller;

import com.medapp.app.dts.medappbackendspring.Dto.*;
import com.medapp.app.dts.medappbackendspring.Entity.Appointment;
import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import com.medapp.app.dts.medappbackendspring.Entity.NotFoundException;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Enum.AppointmentStatus;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import com.medapp.app.dts.medappbackendspring.Service.ClientService;
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
    private ClientService clientService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public ProfileInfoForClient profileForClient(
            @AuthenticationPrincipal User user
    ) {
        return clientService.profileForClient(user);
    }

    @PutMapping("/profile/update")
    public void updateClient(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateClientDto request
    ) {
        clientService.updateClient(user, request);
    }

    @PostMapping("/feedback/create")
    public void addFeedback(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId,
            @RequestBody Feedback feedback
    ) {
        clientService.addFeedback(user, adId, feedback);
    }

    @PutMapping("/feedback/update")
    public void updateFeedback(
            @AuthenticationPrincipal User user,
            @RequestParam Long feedbackId,
            @RequestBody Feedback feedback
    ) {
        clientService.updateFeedback(user, feedbackId, feedback);
    }

    @GetMapping("/feedback/get-all")
    public ResponseEntity<List<FeedbackDto>> getMyFeedbacks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok
                (clientService.getMyFeedbacks(user));
    }

    @DeleteMapping("/feedback/delete")
    public void deleteFeedback(
            @AuthenticationPrincipal User user,
            @RequestParam Long feedbackId
    ) {
        clientService.deleteFeedback(user, feedbackId);
    }

    @GetMapping("/search-doctors")
    public ResponseEntity<List<SearchDoctorResponse>> searchDoctors(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double rating
    ) {
        return ResponseEntity.ok(clientService.searchDoctors(id, firstname, lastname, category, rating));
    }

    @GetMapping("/selected-doctor")
    public ResponseEntity<DoctorProfileInfoForClient> selectedDoctorInfo(
            @RequestParam Long doctorId
    ) {
        return ResponseEntity.ok(clientService.doctorProfileInfoForUser(doctorId));
    }

    @PutMapping("/city/add")
    public void addCityToDoctor(
            @AuthenticationPrincipal User user,
            @RequestParam Long cityId
    ) {
        clientService.addCityToClient(user, cityId);
    }

    @GetMapping("/recommendations")
    public List<GetDoctorAds> getRecommendations(@AuthenticationPrincipal User user) {
        return clientService.getRecomendationAds(user);
    }

    @GetMapping("/ads/ad/category")
    public List<GetDoctorAds> getAdByCategory(@AuthenticationPrincipal User user,
                                              @RequestParam Long categoryId) {
        return clientService.getAdByCategory(user, categoryId);
    }

    @GetMapping("/ads/ad")
    public ResponseEntity<GetDoctorAdById> getDoctorAdById(
            @AuthenticationPrincipal User user,
            @RequestParam Long adId
    ) {
        return ResponseEntity.ok(clientService.getAdById(user, adId));
    }

    @PostMapping("/favorite/add")
    public void addToFavorite(@AuthenticationPrincipal User user,
                              @RequestParam Long adId) {
        clientService.addToFavorite(user, adId);
    }

    @DeleteMapping("/favorite/delete")
    public void deleteFavoriteAd(@AuthenticationPrincipal User user,
                                 @RequestParam Long adId) {
        clientService.removeFromFavorite(user, adId);
    }

    @GetMapping("/favorite/get-all")
    public ResponseEntity<List<FavoriteAdDto>> getAllFavorite(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(clientService.getAllFavorite(user));
    }

    @PostMapping("/ads/ad/appointment/create")
    public void createAppointment(@AuthenticationPrincipal User user,
                                  @RequestParam Long adId,
                                  @RequestBody CreateAppointmentDto body) {
        clientService.createAppointment(user, adId, body);
    }

    @PutMapping("/ads/ad/appointment/update")
    public void updateAppointment(@AuthenticationPrincipal User user,
                                  @RequestParam Long appointmentId,
                                  @RequestBody CreateAppointmentDto body) {
        clientService.updateAppointment(user, appointmentId, body);
    }

    @DeleteMapping("/ads/ad/appointment/delete")
    public void deleteAppointment(@AuthenticationPrincipal User user,
                                  @RequestParam Long appointmentId) {
        clientService.deleteAppointment(user, appointmentId);
    }

    @GetMapping("/ads/ad/appointment/get-all")
    public ResponseEntity<List<MyAppointmentsDto>> getMyAppointments(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(clientService.getMyAppointments(user));
    }

    @GetMapping("/ads/ad/appointment/get-status")
    public ResponseEntity<List<MyAppointmentsDto>> getMyAppointmentsByStatus(@AuthenticationPrincipal User user,
                                                                             @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(clientService.getMyAppointmentsByStatus(user, status));
    }

}
