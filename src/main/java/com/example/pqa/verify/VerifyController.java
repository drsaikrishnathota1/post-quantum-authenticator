package com.example.pqa.verify;

import com.example.pqa.verify.dto.VerifyRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyController {
  private final VerifyService service;

  public VerifyController(VerifyService service) {
    this.service = service;
  }

  @PostMapping("/verify")
  public Map<String, Object> verify(@Valid @RequestBody VerifyRequest req) {
    service.verifyOrThrow(req.username(), req.password());
    return Map.of("status", "ok");
  }

  @ExceptionHandler(VerifyService.UnauthorizedException.class)
  public ResponseEntity<?> unauthorized() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid credentials"));
  }
}

