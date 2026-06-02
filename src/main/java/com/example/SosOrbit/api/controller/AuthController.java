package com.example.SosOrbit.api.controller;

import com.example.SosOrbit.api.dto.LoginDTO;
import com.example.SosOrbit.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid LoginDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
