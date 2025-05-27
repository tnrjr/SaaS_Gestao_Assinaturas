package com.tary.saas.dto;

public record CustomerResponseDTO(
        Long id,
        String name,
        String email,
        String phone
) {}
