package com.ote.keystore.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("Service1Test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class Service1Test {

    @Autowired
    private Service1 service1;

    @Test
    public void check() {
        service1.foo();
    }
}
