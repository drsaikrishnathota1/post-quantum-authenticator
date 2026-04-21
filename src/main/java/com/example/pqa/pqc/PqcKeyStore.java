package com.example.pqa.pqc;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec;
import org.springframework.stereotype.Component;

@Component
public class PqcKeyStore {
  private static final String PROVIDER = "BCPQC";
  private static final String KEY_ALG = "Dilithium";
  private static final String SIG_ALG = "Dilithium";

  private final Map<String, byte[]> privateKeysPkcs8 = new ConcurrentHashMap<>();
  private final Map<String, byte[]> publicKeysX509 = new ConcurrentHashMap<>();

  public GeneratedKeyPair generateDilithium2KeyPair() {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALG, PROVIDER);
      kpg.initialize(DilithiumParameterSpec.dilithium2);
      KeyPair kp = kpg.generateKeyPair();

      String keyId = UUID.randomUUID().toString();
      privateKeysPkcs8.put(keyId, kp.getPrivate().getEncoded());
      publicKeysX509.put(keyId, kp.getPublic().getEncoded());

      return new GeneratedKeyPair(keyId, Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
    } catch (Exception e) {
      throw new IllegalStateException("PQC keygen failed", e);
    }
  }

  public String sign(String keyId, byte[] message) {
    try {
      PrivateKey privateKey = loadPrivateKey(keyId);
      Signature sig = Signature.getInstance(SIG_ALG, PROVIDER);
      sig.initSign(privateKey);
      sig.update(message);
      return Base64.getEncoder().encodeToString(sig.sign());
    } catch (Exception e) {
      throw new IllegalStateException("PQC signing failed", e);
    }
  }

  public boolean verify(String keyId, byte[] message, byte[] signature) {
    try {
      PublicKey publicKey = loadPublicKey(keyId);
      Signature sig = Signature.getInstance(SIG_ALG, PROVIDER);
      sig.initVerify(publicKey);
      sig.update(message);
      return sig.verify(signature);
    } catch (Exception e) {
      return false;
    }
  }

  private PrivateKey loadPrivateKey(String keyId) throws Exception {
    byte[] pkcs8 = privateKeysPkcs8.get(keyId);
    if (pkcs8 == null) throw new IllegalArgumentException("unknown keyId");
    KeyFactory kf = KeyFactory.getInstance(KEY_ALG, PROVIDER);
    return kf.generatePrivate(new PKCS8EncodedKeySpec(pkcs8));
  }

  private PublicKey loadPublicKey(String keyId) throws Exception {
    byte[] x509 = publicKeysX509.get(keyId);
    if (x509 == null) throw new IllegalArgumentException("unknown keyId");
    KeyFactory kf = KeyFactory.getInstance(KEY_ALG, PROVIDER);
    return kf.generatePublic(new X509EncodedKeySpec(x509));
  }

  public record GeneratedKeyPair(String keyId, String publicKeyB64) {}
}

