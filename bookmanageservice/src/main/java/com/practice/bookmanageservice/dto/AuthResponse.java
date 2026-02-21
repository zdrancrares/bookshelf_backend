package com.practice.bookmanageservice.dto;

public record AuthResponse(
        String token,
        String username,
        String email,
        String role
) {}