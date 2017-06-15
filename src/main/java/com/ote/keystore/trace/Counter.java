package com.ote.keystore.trace;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Counter {

    private static AtomicLong Value = new AtomicLong(0L);

    public static long nextValue() {
        return Value.getAndIncrement();
    }
}