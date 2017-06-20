package com.ote.keystore.credential;

import com.ote.keystore.credential.payload.CredentialPayload;
import com.ote.keystore.exceptionhandler.BeanInvalidationResult;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.ote.keystore.JSonUtils.serializeToJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ActiveProfiles({"CredentialRepositoryMock", "CredentialRestControllerTest"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class CredentialRestControllerTest {

    private final static String SECRET_KEY = "forTest";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CredentialRepositoryMock credentialRepositoryMock;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        credentialRepositoryMock.deleteAll();
    }

    //region read
    @Test
    public void reading_by_id_should_return_the_payload_when_exist() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        // NB : payload will be updated and encrypted
        mockMvc.perform(post("/v1/keys/credentials").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload)));

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0").
                param("secretKey", SECRET_KEY)).
                andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        payload.setId(0);
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(payload));
        assertions.assertAll();
    }

    @Test
    public void reading_by_id_with_bad_secret_key_should_raise_decrypt_exception() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        // NB : payload will be updated and encrypted
        mockMvc.perform(post("/v1/keys/credentials").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload)));

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0").param("secretKey", "BAD_KEY")).andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("An error occured while decrypting data");
    }

    @Test
    public void reading_by_id_should_return_not_found_when_not_exist() throws Exception {

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0").param("secretKey", SECRET_KEY)).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("Unable to find user with id 0");
        assertions.assertAll();
    }
    //endregion

    //region create
    @Test
    public void creating_should_return_the_payload_with_a_new_id() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        MvcResult result = mockMvc.perform(post("/v1/keys/credentials").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload))).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        payload.setId(0);
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(payload));
        assertions.assertAll();
    }

    @Test
    public void creating_invalid_payload_should_raise_validation_exception() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword(null); // should raise validation exception
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        MvcResult result = mockMvc.perform(post("/v1/keys/credentials").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload))).
                andReturn();

        BeanInvalidationResult expected = new BeanInvalidationResult();
        expected.setTarget(payload);
        expected.getInvalidations().add(new BeanInvalidationResult.Invalidation("password", "NotNull", "password may not be null"));

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertions.assertThat(result.getResponse().getContentAsString()).isNotNull();
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(expected));

        System.out.println(result.getResponse().getContentAsString());
        assertions.assertAll();
    }
    //endregion

    //region test delete
    @Test
    public void deleting_by_id_should_return_not_found_when_not_exist() throws Exception {

        MvcResult result = mockMvc.perform(delete("/v1/keys/credentials/0")).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("Unable to find user with id 0");
        assertions.assertAll();
    }

    @Test
    public void deleting_by_id_should_return_ok_when_exist() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        mockMvc.perform(post("/v1/keys/credentials").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload))).
                andReturn();

        MvcResult result = mockMvc.perform(delete("/v1/keys/credentials/0")).andReturn();

        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
    //endregion

    //region update
    @Test
    public void putting_should_return_not_found_when_not_exist() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        MvcResult result = mockMvc.perform(put("/v1/keys/credentials/0").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload))).
                andReturn();

        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void patching_should_return_not_found_when_not_exist() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        MvcResult result = mockMvc.perform(patch("/v1/keys/credentials/0").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload))).
                andReturn();

        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void putting_should_replace_entity_when_exist() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        mockMvc.perform(post("/v1/keys/credentials").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload)));

        CredentialPayload payload2 = new CredentialPayload();
        payload2.setId(0);
        payload2.setLogin("newLoginTest");
        payload2.setPassword("newPasswordTest");

        mockMvc.perform(put("/v1/keys/credentials/0").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload2)));

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0").param("secretKey", SECRET_KEY)).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(payload2));
        assertions.assertAll();
    }

    @Test
    public void patching_should_merge_entity_when_exist() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        //create
        mockMvc.perform(post("/v1/keys/credentials").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload)));

        CredentialPayload payload2 = new CredentialPayload();
        payload2.setLogin("newLoginTest");
        payload2.setPassword("newPasswordTest");

        mockMvc.perform(patch("/v1/keys/credentials/0").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload2)));

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0").param("secretKey", SECRET_KEY)).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        payload.setId(0);
        payload.setLogin(payload2.getLogin());
        payload.setPassword(payload2.getPassword());

        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(payload));
        assertions.assertAll();
    }

    @Test
    public void putting_invalid_payload_should_raise_validation_exception() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        mockMvc.perform(post("/v1/keys/credentials").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload)));

        CredentialPayload payload2 = new CredentialPayload();
        payload2.setId(0);
        payload2.setLogin("newLoginTest");
        payload2.setPassword(null); // invalid

        MvcResult result = mockMvc.perform(put("/v1/keys/credentials/0").
                param("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload2))).andReturn();

        BeanInvalidationResult expected = new BeanInvalidationResult();
        expected.setTarget(payload2);
        expected.getInvalidations().add(new BeanInvalidationResult.Invalidation("password", "NotNull", "password may not be null"));

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertions.assertThat(result.getResponse().getContentAsString()).isNotNull();
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(expected));
        assertions.assertAll();
    }
    //endregion


}
