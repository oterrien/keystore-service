package com.ote.keystore.test;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("Service1Test")
@Primary
@Service
public class Service2Mock extends Service2 {

    @Override
    public void foo() {
        System.out.println("mock");
    }
}
