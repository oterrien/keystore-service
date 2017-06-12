package com.ote.keystore.credential;

import com.ote.keystore.credential.model.CredentialPayload;
import com.ote.keystore.credential.persistence.CredentialEntity;
import com.ote.keystore.credential.persistence.CredentialPersistenceService;
import com.ote.keystore.cryptor.CryptorService;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles({"CredentialPersistenceServiceTest"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class CredentialPersistenceServiceTest {

    private final static String SECRET_KEY = "forTest";

    @Autowired
    private CredentialPersistenceService credentialPersistenceService;

    @Autowired
    private CryptorService cryptorService;

    // region READ
    @Test
    @Sql({"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql", "classpath:populate_encrypted_credential.sql"})
    public void reading_by_id_should_return_the_entity_when_exist() throws Exception {

        CredentialEntity entity = credentialPersistenceService.find(0);
        Assertions.assertThat(entity).isNotNull();

    }

    @Test(expected = CredentialPersistenceService.NotFoundException.class)
    @Sql(scripts = {"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql"})
    public void reading_by_id_should_raise_not_found_exception_when_not_exist() throws Exception {

        credentialPersistenceService.find(0);
        Assertions.fail("CredentialPersistenceService.NotFoundException should be raised");
    }
    // endregion

    // region CREATE
    @Test
    @Sql({"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql", "classpath:populate_encrypted_credential.sql"})
    public void creating_should_return_the_entity_with_increased_id() throws Exception {

        CredentialEntity entity = new CredentialEntity();
        entity.setLogin("newLogin");
        entity.setPassword("newPassword");
        entity.setApplication("newApplication");
        entity.setDescription("newDescription");

        CredentialEntity resultCreate = credentialPersistenceService.create(entity, SECRET_KEY);
        Assertions.assertThat(resultCreate).isNotNull();
        Assertions.assertThat(resultCreate.getId()).isEqualTo(1);

        CredentialEntity resultFind = credentialPersistenceService.find(1);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(resultFind).isNotNull();
        assertions.assertThat(resultCreate).isEqualTo(resultFind);
        assertions.assertThat(credentialPersistenceService.count()).isEqualTo(2);
        assertions.assertAll();
    }
    // endregion

    // region UPDATE
    @Test
    @Sql({"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql", "classpath:populate_encrypted_credential.sql"})
    public void reseting_should_replace_existing_entity() throws Exception {

        CredentialEntity entity = new CredentialEntity();
        entity.setLogin("newLogin");
        entity.setPassword("newPassword");
        entity.setApplication("newApplication");
        entity.setDescription("newDescription");

        CredentialEntity resultReset = credentialPersistenceService.reset(0, entity, SECRET_KEY);
        CredentialEntity resultFind = credentialPersistenceService.find(0);

        resultReset = cryptorService.decrypt(SECRET_KEY, resultReset);
        resultFind = cryptorService.decrypt(SECRET_KEY, resultFind);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(resultReset).isNotNull();
        assertions.assertThat(resultReset).isEqualTo(resultFind);
        assertions.assertThat(resultFind.getLogin()).isEqualTo("newLogin");
        assertions.assertThat(credentialPersistenceService.count()).isEqualTo(1);
        assertions.assertAll();
    }

    @Test(expected = CredentialPersistenceService.NotFoundException.class)
    @Sql(scripts = {"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql"})
    public void reseting_should_raise_not_found_exception_when_not_exist() throws Exception {

        CredentialEntity entity = new CredentialEntity();
        entity.setLogin("newLogin");
        entity.setPassword("newPassword");
        entity.setApplication("newApplication");
        entity.setDescription("newDescription");

        credentialPersistenceService.reset(0, entity, SECRET_KEY);
        Assertions.fail("CredentialPersistenceService.NotFoundException should be raised");
    }

    @Test
    @Sql({"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql", "classpath:populate_encrypted_credential.sql"})
    public void patching_should_merge_entities() throws Exception {

        CredentialEntity entity = new CredentialEntity();
        entity.setLogin("newLogin");
        entity.setPassword("newPassword");

        CredentialEntity resultMerge = credentialPersistenceService.merge(0, entity, SECRET_KEY);
        CredentialEntity resultFind = credentialPersistenceService.find(0);

        resultMerge = cryptorService.decrypt(SECRET_KEY, resultMerge);
        resultFind = cryptorService.decrypt(SECRET_KEY, resultFind);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(resultMerge).isNotNull();
        assertions.assertThat(resultMerge).isEqualTo(resultFind);
        assertions.assertThat(resultFind.getLogin()).isEqualTo("newLogin");
        assertions.assertThat(resultFind.getPassword()).isEqualTo("newPassword");
        assertions.assertThat(resultFind.getApplication()).isNotNull();
        assertions.assertThat(resultFind.getDescription()).isNotNull();
        assertions.assertThat(credentialPersistenceService.count()).isEqualTo(1);
        assertions.assertAll();
    }

    @Test(expected = CredentialPersistenceService.NotFoundException.class)
    @Sql(scripts = {"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql"})
    public void merging_should_raise_not_found_exception_when_not_exist() throws Exception {

        CredentialEntity entity = new CredentialEntity();
        entity.setLogin("newLogin");
        entity.setPassword("newPassword");
        entity.setApplication("newApplication");
        entity.setDescription("newDescription");

        credentialPersistenceService.merge(0, entity, SECRET_KEY);
        Assertions.fail("CredentialPersistenceService.NotFoundException should be raised");
    }
    // endregion

    // region DELETE
    @Test
    @Sql({"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql", "classpath:populate_encrypted_credential.sql"})
    public void deleting_by_id_should_remove_the_entity_when_exist() throws Exception {

        credentialPersistenceService.delete(0);
        Assertions.assertThat(credentialPersistenceService.count()).isEqualTo(0);
    }

    @Test(expected = CredentialPersistenceService.NotFoundException.class)
    @Sql({"classpath:clean_credential.sql", "classpath:clean_credential_sequence.sql", "classpath:populate_encrypted_credential.sql"})
    public void deleting_by_id_should_raised_not_found_exception_when_not_exist() throws Exception {

        credentialPersistenceService.delete(1); // ID=1 does not exist
        Assertions.fail("CredentialPersistenceService.NotFoundException should be raised");
    }
    // endregion

    @Ignore
    @Test
    public void generateInsertEncrypted() {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        System.out.println(cryptorService.encrypt(SECRET_KEY, payload));
    }
}
