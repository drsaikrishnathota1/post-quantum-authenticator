package com.example.pqa.pqc;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PqcConfig {
  static {
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
    if (Security.getProvider(BouncyCastlePQCProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastlePQCProvider());
    }
  }
}

