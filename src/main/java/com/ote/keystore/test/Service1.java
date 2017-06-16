package com.ote.keystore.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Service1 {

    @Autowired
    private Service2 service2;

    public void foo(){
        service2.foo();
    }
}
