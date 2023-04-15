package com.medapp.app.dts.medappbackendspring.Controller;

import com.medapp.app.dts.medappbackendspring.Dto.*;
import com.medapp.app.dts.medappbackendspring.Service.AuthService;
import com.medapp.app.dts.medappbackendspring.Service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final LogoutService logoutService;

    @PostMapping("/create-client")
    public ResponseEntity<ReturnedTokenDto> registerClient(
            @RequestBody RegisterClientDto request
    ) {
        return ResponseEntity.ok(authService.registerClient(request));
    }

    @PostMapping("/create-doctor")
    public ResponseEntity<ReturnedTokenDto> registerDoctor(
            @RequestBody RegisterDoctorDto request
    ) {
        return ResponseEntity.ok(authService.registerDoctor(request));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<ReturnedTokenDto> registerAdmin(
            @RequestBody RegisterAdminDto request
    ) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ReturnedTokenDto> authenticate(
            @RequestBody AuthenticationDto request

    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok().build();
    }

}
