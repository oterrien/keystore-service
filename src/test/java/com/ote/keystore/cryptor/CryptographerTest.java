package com.ote.keystore.cryptor;

import com.ote.keystore.credential.persistence.CredentialEntity;
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
    public void encrypting_then_decrypting_entity_with_same_secretkey_should_return_the_original_entity() throws Exception {

        CredentialEntity entity = new CredentialEntity();
        entity.setId(0);
        entity.setLogin("newLogin");
        entity.setPassword("newPassword");
        entity.setApplication("newApplication");
        entity.setDescription("newDescription");

        CredentialEntity entityEncrypted = cryptorService.encrypt(SECRET_KEY, entity);

        Assertions.assertThat(entityEncrypted).isNotNull();

        CredentialEntity entityDecrypted = cryptorService.decrypt(SECRET_KEY, entityEncrypted);

        Assertions.assertThat(entityDecrypted).isEqualTo(entity);
    }


    @Test
    public void encrypt_same_value_should_result_same_value() {

        String test = "newTest";
        Assertions.assertThat(cryptorService.encrypt(SECRET_KEY, test)).isEqualTo(cryptorService.encrypt(SECRET_KEY, test));
    }

}
