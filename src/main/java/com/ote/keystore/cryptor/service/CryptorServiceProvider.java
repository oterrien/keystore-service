package com.ote.keystore.cryptor.service;

import com.ote.keystore.cryptor.Cryptable;
import com.ote.keystore.cryptor.annotation.Decrypt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CryptorServiceProvider {

    @Getter
    public static CryptorService cryptorServiceInstance;

    public CryptorServiceProvider(@Autowired CryptorService cryptorService) {
        cryptorServiceInstance = cryptorService;
    }

    @RequiredArgsConstructor
    public static class Decrypter<T extends Cryptable> implements Decrypt.IDecrypter<T> {

        private final String secretKey;

        @Override
        public T convertTo(T obj) {

            if (obj.isEncrypted()) {
                return getCryptorServiceInstance().decrypt(obj, secretKey);
            } else {
                return obj;
            }
        }
    }

}
