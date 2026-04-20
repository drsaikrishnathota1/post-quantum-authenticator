package com.example.pqa.verify;

import com.example.pqa.pqc.PqcKeyStore;
import java.nio.charset.StandardCharsets;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class VerifyService {
  private final VerifyProperties props;
  private final Argon2PasswordEncoder encoder;
  private final PqcKeyStore pqcKeyStore;

  public VerifyService(VerifyProperties props, PqcKeyStore pqcKeyStore) {
    this.props = props;
    this.pqcKeyStore = pqcKeyStore;
    this.encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }

  public void verifyOrThrow(String username, String password) {
    String u = username == null ? "" : username.trim();
    String p = password == null ? "" : password.trim();
    if (!props.username().equals(u)) {
      throw new UnauthorizedException();
    }
    String configured = props.passwordHash();
    String effectiveHash = configured != null && configured.startsWith("$argon2")
        ? configured
        : encoder.encode(configured == null ? "" : configured);

    if (!encoder.matches(p, effectiveHash)) {
      throw new UnauthorizedException();
    }

    // PQC "on top": perform a Dilithium sign+verify round-trip as part of the auth flow.
    // This demonstrates PQC integration without requiring additional client-side steps.
    var kp = pqcKeyStore.generateDilithium2KeyPair();
    byte[] msg = (u + ":authenticated").getBytes(StandardCharsets.UTF_8);
    String sigB64 = pqcKeyStore.sign(kp.keyId(), msg);
    boolean ok = pqcKeyStore.verify(kp.keyId(), msg, java.util.Base64.getDecoder().decode(sigB64));
    if (!ok) {
      throw new IllegalStateException("PQC verification failed");
    }
  }

  public static class UnauthorizedException extends RuntimeException {}
}

