package com.ote.keystore.cryptor;

import com.ote.keystore.credential.payload.CredentialPayload;
import com.ote.keystore.cryptor.service.CryptorService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles({"CryptographerTest"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class CryptographerTest {

    public static final String SECRET_KEY = "secret";

    @Autowired
    private CryptorService cryptorService;

    @Test
    public void encrypting_then_decrypting_with_same_secretkey_should_return_the_original_entity() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setId(0);
        payload.setLogin("newLogin");
        payload.setPassword("newPassword");
        payload.setApplication("newApplication");
        payload.setDescription("newDescription");
        payload.setEncrypted(false);

        CredentialPayload encryptedPayload = cryptorService.encrypt(payload, SECRET_KEY);

        Assertions.assertThat(encryptedPayload).isNotNull();
        Assertions.assertThat(encryptedPayload.isEncrypted()).isEqualTo(true);

        CredentialPayload decryptedPayload = cryptorService.decrypt(encryptedPayload, SECRET_KEY);

        Assertions.assertThat(decryptedPayload).isEqualTo(payload);
        Assertions.assertThat(decryptedPayload.isEncrypted()).isEqualTo(false);
    }


    @Test
    public void encrypt_same_value_should_result_same_value() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setId(0);
        payload.setLogin("newLogin");
        payload.setPassword("newPassword");
        payload.setApplication("newApplication");
        payload.setDescription("newDescription");

        Assertions.assertThat(cryptorService.encrypt(payload, SECRET_KEY)).isEqualTo(cryptorService.encrypt(payload, SECRET_KEY));
    }

}
