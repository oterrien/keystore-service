package com.ote.keystore.cryptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation aims to specify which parameter has to be encypted
 * The value() aims to specify the key in Spel language.
 * In the most of time, #secretKey would refer to the parameter secretKey of the same method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Encrypt {

    String value();
}
