package com.example.pqa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PostQuantumAuthenticatorApplication {
  public static void main(String[] args) {
    SpringApplication.run(PostQuantumAuthenticatorApplication.class, args);
  }
}

