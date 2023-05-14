package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Dto.*;
import com.medapp.app.dts.medappbackendspring.Entity.Token;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Enum.Role;
import com.medapp.app.dts.medappbackendspring.Enum.TokenType;
import com.medapp.app.dts.medappbackendspring.Repository.TokenRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ReturnedTokenDto registerClient(RegisterClientDto request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER_CLIENT)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return ReturnedTokenDto.builder()
                .token(jwtToken)
                .user_type(user.getRole().toString())
                .build();
    }

    public ReturnedTokenDto registerDoctor(RegisterDoctorDto request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER_DOCTOR)
                .rating(0.0)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return ReturnedTokenDto.builder()
                .token(jwtToken)
                .user_type(user.getRole().toString())
                .build();
    }

    public ReturnedTokenDto registerAdmin(RegisterAdminDto request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER_ADMIN)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return ReturnedTokenDto.builder()
                .token(jwtToken)
                .user_type(user.getRole().toString())
                .build();
    }

    public ReturnedTokenDto authenticate(AuthenticationDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return ReturnedTokenDto.builder()
                .token(jwtToken)
                .user_type(user.getRole().toString())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


}
