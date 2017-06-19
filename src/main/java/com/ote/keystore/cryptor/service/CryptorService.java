package com.ote.keystore.cryptor.service;

import com.ote.keystore.cryptor.Cryptable;
import com.ote.keystore.cryptor.annotation.Crypted;
import com.ote.keystore.cryptor.annotation.Decrypt;
import com.ote.keystore.cryptor.annotation.SecretKey;
import com.ote.keystore.trace.annotation.Traceable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

@Service
public class CryptorService {

    private static final String ALGORITHM = "AES";

    @Traceable(level = Traceable.Level.TRACE)
    public <T extends Cryptable> T encrypt(T object, @SecretKey String key) throws EncryptException {

        if (object.isEncrypted()) {
            return object;
        }

        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            doCrypt(object, (field) -> encrypt(field, object, cipher));
            object.setEncrypted(true);
            return object;
        } catch (Exception e) {
            throw new EncryptException(e);
        }
    }

    @Traceable(level = Traceable.Level.TRACE)
    public <T extends Cryptable> T decrypt(T object, @SecretKey String key) throws DecryptException {

        if (!object.isEncrypted()) {
            return object;
        }

        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            doCrypt(object, (field) -> decrypt(field, object, cipher));
            object.setEncrypted(false);
            return object;
        } catch (Exception e) {
            throw new DecryptException(e);
        }
    }

    private void doCrypt(Object object, Consumer<Field> cryptingMethod) {
        Arrays.stream(object.getClass().getDeclaredFields()).
                filter(field -> field.isAnnotationPresent(Crypted.class)). // get only fields annotated with @Crypted
                peek(field -> field.setAccessible(true)). // set each such field accessible
                filter(field -> isFieldValueNonNull(field, object)). // get only fields where value is not null
                forEach(cryptingMethod); // apply given cryptingMethod for each remaining field
    }

    private boolean isFieldValueNonNull(Field field, Object object) {
        try {
            return field.get(object) != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void encrypt(Field field, Object object, Cipher cipher) {
        try {
            field.set(object, new String(Base64.encodeBase64(cipher.doFinal(((String) field.get(object)).getBytes()))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void decrypt(Field field, Object object, Cipher cipher) {
        try {
            field.set(object, new String(cipher.doFinal(Base64.decodeBase64((String) field.get(object)))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class EncryptException extends RuntimeException {

        private final static String MESSAGE = "An error occured while encrypting data";

        public EncryptException(String message) {
            super(MESSAGE + " : " + message);
        }

        public EncryptException(Exception e) {
            super(MESSAGE, e);
        }
    }

    public static class DecryptException extends RuntimeException {

        private final static String MESSAGE = "An error occured while decrypting data";

        public DecryptException(String message) {
            super(MESSAGE + " : " + message);
        }

        public DecryptException(Exception e) {
            super(MESSAGE, e);
        }
    }


}
