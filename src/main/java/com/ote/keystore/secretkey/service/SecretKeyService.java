package com.ote.keystore.secretkey.service;

import com.ote.keystore.trace.annotation.Traceable;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SecretKeyService {

    private static final char secretKeyDefaultPadChar = 'X';

    private static final int secretKeyDefaultBits = 128;

    @Traceable(level = Traceable.Level.TRACE)
    public String getSecretKey(String secretKey) {
        int size = secretKeyDefaultBits / 8;
        return StringUtils.rightPad(secretKey, size, secretKeyDefaultPadChar);
    }

    public String hash(String secretKey){
        return DigestUtils.sha256Hex(secretKey);
    }
}
