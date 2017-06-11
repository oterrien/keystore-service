package com.ote.keystore.cryptor;

import com.ote.keystore.credential.persistence.CredentialEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class CryptorService {
    public static final String ALGORITHM = "AES";

    public CredentialEntity encrypt(String key, CredentialEntity entity) throws EncryptException {

        try {
            key = StringUtils.rightPad(key, 16, "X"); // 128 bit key
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            entity.setLogin(Base64.encodeBase64String(cipher.doFinal(entity.getLogin().getBytes())));
            entity.setPassword(Base64.encodeBase64String(cipher.doFinal(entity.getPassword().getBytes())));
            entity.setApplication(Base64.encodeBase64String(cipher.doFinal(entity.getApplication().getBytes())));
            entity.setDescription(Base64.encodeBase64String(cipher.doFinal(entity.getDescription().getBytes())));

            return entity;
        } catch (Exception e) {
            throw new EncryptException(e);
        }
    }

    public CredentialEntity decrypt(String key, CredentialEntity entity) throws DecryptException {

        try {
            key = StringUtils.rightPad(key, 16, "X"); // 128 bit key
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            entity.setLogin(new String(cipher.doFinal(Base64.decodeBase64(entity.getLogin()))));
            entity.setPassword(new String(cipher.doFinal(Base64.decodeBase64(entity.getPassword()))));
            entity.setApplication(new String(cipher.doFinal(Base64.decodeBase64(entity.getApplication()))));
            entity.setDescription(new String(cipher.doFinal(Base64.decodeBase64(entity.getDescription()))));

            return entity;
        } catch (Exception e) {
            throw new DecryptException(e);
        }
    }

    public static class EncryptException extends Exception {
        public EncryptException(Exception e) {
            super("An error occured while encrypting data", e);
        }
    }

    public static class DecryptException extends Exception {
        public DecryptException(Exception e) {
            super("An error occured while decrypting data", e);
        }
    }

    //region encrypt AES/CBC/PKCS5PADDING with 2 keys
    /*public CredentialEntity encrypt(String key, String initVector, CredentialEntity entity) throws EncryptException {

        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            entity.setLogin(Base64.encodeBase64String(cipher.doFinal(entity.getLogin().getBytes())));
            entity.setPassword(Base64.encodeBase64String(cipher.doFinal(entity.getPassword().getBytes())));
            entity.setApplication(Base64.encodeBase64String(cipher.doFinal(entity.getApplication().getBytes())));
            entity.setDescription(Base64.encodeBase64String(cipher.doFinal(entity.getDescription().getBytes())));

            return entity;
        } catch (Exception e) {
            throw new EncryptException(e);
        }
    }

    public CredentialEntity decrypt(String key, String initVector, CredentialEntity entity) throws DecryptException {

        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            entity.setLogin(new String(cipher.doFinal(Base64.decodeBase64(entity.getLogin()))));
            entity.setPassword(new String(cipher.doFinal(Base64.decodeBase64(entity.getPassword()))));
            entity.setApplication(new String(cipher.doFinal(Base64.decodeBase64(entity.getApplication()))));
            entity.setDescription(new String(cipher.doFinal(Base64.decodeBase64(entity.getDescription()))));

            return entity;
        } catch (Exception e) {
            throw new DecryptException(e);
        }
    }*/
    //endregion
}
