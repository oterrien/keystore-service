package com.ote.keystore.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("TestAspect")
@Service
public class TestService {

    @TestAnnotation
    public String foo() {
        return "Foo";
    }

    @TestAnnotation
    public List<String> foo1() {
        return Stream.of("Foo", "Bar").collect(Collectors.toList());
    }

    @TestAnnotation
    public Page<Payload> foo11() {

        return new PageImpl<>(Stream.of(new Payload("Foo"), new Payload("Bar")).collect(Collectors.toList()));
    }

    @Data
    @AllArgsConstructor
    public static class Payload {
        private String value;
    }

    @TestAnnotation
    public Map<Long, String> foo2() {

        Map<Long, String> map = new HashMap<>();
        map.put(1L, "Foo");
        map.put(2L, "Bar");
        return map;
    }
}
