package com.ote.keystore.cryptor.service;

import com.ote.keystore.cryptor.annotation.Crypted;
import com.ote.keystore.cryptor.annotation.SecretKey;
import com.ote.keystore.trace.annotation.Traceable;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

@Service
public class CryptorService {

    public static final String ALGORITHM = "AES";

    @Traceable(level = Traceable.Level.TRACE)
    public <T> T encrypt(@SecretKey String key, T object) throws EncryptException {

        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            Arrays.stream(object.getClass().getDeclaredFields()).
                    filter(p -> p.isAnnotationPresent(Crypted.class)).
                    forEach(p -> {
                        try {
                            p.setAccessible(true);
                            if (p.get(object) != null) {
                                p.set(object, new String(Base64.encodeBase64(cipher.doFinal(((String) p.get(object)).getBytes()))));
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
            return object;
        } catch (Exception e) {
            throw new EncryptException(e);
        }
    }

    @Traceable(level = Traceable.Level.TRACE)
    public <T> T decrypt(@SecretKey String key, T object) throws DecryptException {

        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            Arrays.stream(object.getClass().getDeclaredFields()).
                    filter(p -> p.isAnnotationPresent(Crypted.class)).
                    forEach(p -> {
                        try {
                            p.setAccessible(true);
                            if (p.get(object) != null) {
                                p.set(object, new String(cipher.doFinal(Base64.decodeBase64((String) p.get(object)))));
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
            return object;
        } catch (Exception e) {
            throw new DecryptException(e);
        }
    }

    public static class EncryptException extends RuntimeException {
        public EncryptException(Exception e) {
            super("An error occured while encrypting data", e);
        }
    }

    public static class DecryptException extends RuntimeException {
        public DecryptException(Exception e) {
            super("An error occured while decrypting data", e);
        }
    }
}
