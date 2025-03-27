package com.hyperativa.visa.infrastructure.service;

import com.hyperativa.visa.domain.service.CryptoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Value("${crypto.key}")
    private String cryptoKey;

    @Value("${crypto.init-vector")
    private String initVector;

    @Override
    public String encrypt(final String data) throws Exception {
        final var iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
        final var sKeySpec = new SecretKeySpec(cryptoKey.getBytes(StandardCharsets.UTF_8), "AES");

        final var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
