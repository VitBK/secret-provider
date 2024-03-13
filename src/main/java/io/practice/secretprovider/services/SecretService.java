package io.practice.secretprovider.services;

import io.practice.secretprovider.model.SecretEntity;
import io.practice.secretprovider.repositories.SecretRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class SecretService {

    @Autowired
    private SecretRepository secretRepository;
    private Cipher encryptor;
    private Cipher decryptor;

    @PostConstruct
    private void init() throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKey key = generateKey(128);
        IvParameterSpec ivParameterSpec = generateIv();
        var algorithm = "AES/CBC/PKCS5Padding";
        encryptor = Cipher.getInstance(algorithm);
        encryptor.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        decryptor = Cipher.getInstance(algorithm);
        decryptor.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
    }

    @Transactional
    public SecretEntity createSecret(String input) {
        String encodedSecret = encrypt(input);
        return secretRepository.save(new SecretEntity(encodedSecret));
    }

    @Transactional
    public String getSecret(UUID id) {
        var se = secretRepository.findById(id).orElse(null);
        if (se == null) {
            return "No such secret in our service!";
        }
        secretRepository.delete(se);
        return decrypt(se.getEncodedSecret());
    }

    private String encrypt(String input) {
        byte[] cipherText;
        try {
            cipherText = encryptor.doFinal(input.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    private String decrypt(String cipherText) {
        byte[] plainText;
        try {
            plainText = decryptor.doFinal(Base64.getDecoder()
                    .decode(cipherText));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return new String(plainText);
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }
}
