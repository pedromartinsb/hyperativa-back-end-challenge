package com.hyperativa.visa.infrastructure.service;

import com.hyperativa.visa.domain.service.CryptoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Value("${crypto.key}")
    private String cryptoKey;

    private static final String INIT_VECTOR = "1234567890abcdef";

    @Override
    public String encrypt(final String data) throws Exception {
        byte[] ivBytes = new byte[16];

        final var secureRandom = new SecureRandom();
        secureRandom.nextBytes(ivBytes);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        final var sKeySpec = new SecretKeySpec(cryptoKey.getBytes(StandardCharsets.UTF_8), "AES");
        final var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
