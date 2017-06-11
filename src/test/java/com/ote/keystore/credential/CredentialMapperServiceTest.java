package com.ote.keystore.credential;

import com.ote.keystore.credential.mapper.CredentialMapperService;
import com.ote.keystore.credential.model.CredentialPayload;
import com.ote.keystore.credential.persistence.CredentialEntity;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles({"CredentialMapperServiceTest"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class CredentialMapperServiceTest {

    @Autowired
    private CredentialMapperService credentialMapperService;

    @Test
    public void dual_payload_conversion_should_return_same_result(){

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        CredentialPayload result = credentialMapperService.convert(credentialMapperService.convert(payload));

        Assertions.assertThat(result).isEqualTo(payload);
    }

    @Test
    public void dual_entity_conversion_should_return_same_result(){

        CredentialEntity entity = new CredentialEntity();
        entity.setLogin("loginTest");
        entity.setPassword("passwordTest");
        entity.setApplication("applicationTest");
        entity.setDescription("descriptionTest");

        CredentialEntity result = credentialMapperService.convert(credentialMapperService.convert(entity));

        Assertions.assertThat(result).isEqualTo(entity);
    }
}
