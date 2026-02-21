package com.practice.bookmanageservice.service;

import com.practice.bookmanageservice.dto.AuthResponse;
import com.practice.bookmanageservice.dto.LoginRequest;
import com.practice.bookmanageservice.dto.RegisterRequest;
import com.practice.bookmanageservice.entity.User;
import com.practice.bookmanageservice.repository.UserRepository;
import com.practice.bookmanageservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(User.Role.USER)
                .build();
        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        var user = userRepository.findByUsername(request.username()).orElseThrow();
        var token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole().name());
    }
}