package com.ote.keystore.merger;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class BeanMergerService {

    public void copyNonNullProperties(Object source, Object dest) throws IllegalAccessException, InvocationTargetException {

        new BeanUtilsBean() {

            @Override
            public void copyProperty(Object dest, String name, Object value)
                    throws IllegalAccessException, InvocationTargetException {
                if (value != null) {
                    super.copyProperty(dest, name, value);
                }
            }
        }.copyProperties(dest, source);
    }

    public void copyAllProperties(Object source, Object dest) throws IllegalAccessException, InvocationTargetException {

        new BeanUtilsBean() {

            @Override
            public void copyProperty(Object dest, String name, Object value)
                    throws IllegalAccessException, InvocationTargetException {
                super.copyProperty(dest, name, value);
            }
        }.copyProperties(dest, source);
    }
}
