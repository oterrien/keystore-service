package com.ote.keystore.credential;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ote.keystore.credential.model.CredentialPayload;
import com.ote.keystore.cryptor.service.CryptorService;
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

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ActiveProfiles({"CredentialRepositoryMock", "CredentialRestControllerTest"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class CredentialRestControllerTest {

    private final static String SECRET_KEY = "forTest";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private CredentialRepositoryMock credentialRepositoryMock;

    @Autowired
    private CryptorService cryptorService;

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
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone())));

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0")).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        payload.setId(0);
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(cryptorService.encrypt(SECRET_KEY, payload)));
        assertions.assertAll();
    }

    @Test
    public void reading_by_id_should_return_not_found_when_not_exist() throws Exception {

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0")).andReturn();

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
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone()))).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        payload.setId(0);
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(cryptorService.encrypt(SECRET_KEY, payload)));
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
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone()))).
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
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone()))).
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
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone()))).
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
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone()))).
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
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone())));

        CredentialPayload payload2 = new CredentialPayload();
        payload2.setId(0);
        payload2.setLogin("newLoginTest");
        payload2.setPassword("newPasswordTest");

        mockMvc.perform(put("/v1/keys/credentials/0").
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload2.clone())));

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0")).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(cryptorService.encrypt(SECRET_KEY, payload2)));
        assertions.assertAll();
    }

    @Test
    public void patching_should_merge_entity_when_exist() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        mockMvc.perform(post("/v1/keys/credentials").
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone())));

        CredentialPayload payload2 = new CredentialPayload();
        payload2.setId(0);
        payload2.setLogin("newLoginTest");
        payload2.setPassword("newPasswordTest");

        mockMvc.perform(patch("/v1/keys/credentials/0").
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload2.clone())));

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0")).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        payload.setId(0);
        payload.setLogin(payload2.getLogin());
        payload.setPassword(payload2.getPassword());

        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(cryptorService.encrypt(SECRET_KEY, payload)));
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
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload.clone())));

        CredentialPayload payload2 = new CredentialPayload();
        payload2.setId(0);
        payload2.setLogin("newLoginTest");
        payload2.setPassword(null); // invalid

        MvcResult result = mockMvc.perform(put("/v1/keys/credentials/0").
                header("secretKey", SECRET_KEY).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload2.clone()))).andReturn();

        BeanInvalidationResult expected = new BeanInvalidationResult();
        expected.setTarget(payload2);
        expected.getInvalidations().add(new BeanInvalidationResult.Invalidation("password", "NotNull", "password may not be null"));

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertions.assertThat(result.getResponse().getContentAsString()).isNotNull();
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(serializeToJson(cryptorService.encrypt(SECRET_KEY, expected)));

        System.out.println(result.getResponse().getContentAsString());
        assertions.assertAll();
    }


    //endregion

    public static <T> T parseFromJson(String CredentialPayloadAsJson, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(CredentialPayloadAsJson, clazz);
    }

    public static <T> String serializeToJson(T CredentialPayloadAsJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(CredentialPayloadAsJson);
    }
}
