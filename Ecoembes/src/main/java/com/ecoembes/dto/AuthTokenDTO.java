package com.ecoembes.dto;

public record AuthTokenDTO(
        String token,
        long timestamp
) {}