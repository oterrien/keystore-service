package com.ote.keystore.aop;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@ActiveProfiles("TestAspect")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestAspectTest {

    @Autowired
    private TestService testService;

    @Test
    public void check_foo() {
        log.warn("########################## " + testService.foo());
    }

    @Test
    public void check_foo1() {
        log.warn("########################## " + testService.foo1());
    }

    @Test
    public void check_foo11() throws Exception{
        log.warn("########################## " + serializeToJson(testService.foo11()));
    }

    @Test
    public void check_foo2() {
        log.warn("########################## " + testService.foo2());
    }

    public static <T> String serializeToJson(T CredentialPayloadAsJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(CredentialPayloadAsJson);
    }

}
