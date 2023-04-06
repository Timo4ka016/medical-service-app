package com.medapp.app.dts.medappbackendspring.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/doctor")
    @PreAuthorize("hasAuthority('USER_DOCTOR')")
    public ResponseEntity<String> doctor() {
        return ResponseEntity.ok("Doctor");
    }

    @GetMapping("/client")
    @PreAuthorize("hasAuthority('USER_CLIENT')")
    public ResponseEntity<String> client() {
        return ResponseEntity.ok("Client");
    }

}
