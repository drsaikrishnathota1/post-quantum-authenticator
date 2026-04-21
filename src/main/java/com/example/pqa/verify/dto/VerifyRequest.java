package com.example.pqa.verify.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyRequest(@NotBlank String username, @NotBlank String password) {}

