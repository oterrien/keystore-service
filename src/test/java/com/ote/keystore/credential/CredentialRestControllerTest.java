package com.ote.keystore.credential;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ote.keystore.credential.model.CredentialPayload;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles({"CredentialPersistenceServiceMock", "CredentialRestControllerTest"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class CredentialRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private CredentialPersistenceServiceMock credentialPersistenceServiceMock;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        credentialPersistenceServiceMock.cleanRepositoryMock();
    }

    @Test
    public void readingItByIdShouldReturnThePayloadWhenExist() throws Exception {

        CredentialPayload payload = new CredentialPayload();
        payload.setLogin("loginTest");
        payload.setPassword("passwordTest");
        payload.setApplication("applicationTest");
        payload.setDescription("descriptionTest");

        mockMvc.perform(post("/v1/keys/credentials").
                contentType(MediaType.APPLICATION_JSON_VALUE).
                content(serializeToJson(payload))).andReturn();

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0")).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        payload.setId(0);
        assertions.assertThat(parseFromJson(result.getResponse().getContentAsString())).isEqualTo(payload);
        assertions.assertAll();
    }

    @Test
    public void readingItByIdShouldReturnNotFoundExceptionWhenNotExist() throws Exception {

        MvcResult result = mockMvc.perform(get("/v1/keys/credentials/0")).andReturn();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("Unable to find user with id 0");
        assertions.assertAll();
    }

    public CredentialPayload parseFromJson(String CredentialPayloadAsJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(CredentialPayloadAsJson, CredentialPayload.class);
    }

    public String serializeToJson(CredentialPayload CredentialPayloadAsJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(CredentialPayloadAsJson);
    }
}
