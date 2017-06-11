package com.ote.keystore.cryptor;

import com.ote.keystore.credential.persistence.CredentialEntity;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles({"CryptographerTest"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class CryptographerTest {

    @Autowired
    private CryptorService cryptorService;

    @Test
    public void encrypting_then_decrypting_entity_with_same_secretkey_should_return_the_original_entity() throws Exception{

        CredentialEntity entity = new CredentialEntity();
        entity.setId(0);
        entity.setLogin("newLogin");
        entity.setPassword("newPassword");
        entity.setApplication("newApplication");
        entity.setDescription("newDescription");

        String key = StringUtils.rightPad("secret", 16, "X"); // 128 bit key

        CredentialEntity entityEncrypted = cryptorService.encrypt(key, entity);

        Assertions.assertThat(entityEncrypted).isNotNull();

        CredentialEntity entityDecrypted = cryptorService.decrypt(key, entityEncrypted);

        Assertions.assertThat(entityDecrypted).isEqualTo(entity);
    }

    @Test
    public void reading_data_without_passphrase_should_not_decrypt(){

    }

}
