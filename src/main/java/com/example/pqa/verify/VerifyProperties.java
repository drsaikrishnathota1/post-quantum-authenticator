package com.example.pqa.verify;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.user")
public record VerifyProperties(String username, String passwordHash) {}

