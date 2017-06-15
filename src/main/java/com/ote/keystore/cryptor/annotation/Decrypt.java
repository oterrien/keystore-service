package com.ote.keystore.cryptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This annotation aims to specify which parameter has to be encypted
 * The secretKey() aims to specify the key in Spel language.
 * In the most of time, #secretKey would refer to the parameter secretKey of the same method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Decrypt {

    String secretKey();

    Class<? extends IDecrypter> decrypter();

    Class<? extends IUpdater> updater() default Identity.class;


    interface IUpdater<TC, T> {
        TC update(TC orig, IDecrypter<T> converter);
    }

    interface IDecrypter<T> {
        T convertTo(T obj);
    }

    class Identity implements IUpdater {
        @Override
        public Object update(Object orig, IDecrypter converter) {
            return converter.convertTo(orig);
        }
    }

    class ListUpdater<T> implements IUpdater<List<T>, T> {

        @Override
        public List<T> update(List<T> orig, IDecrypter<T> converter) {
            return orig.stream().
                    map(converter::convertTo).
                    collect(Collectors.toList());
        }
    }

    class MapValueUpdater<TK, TV> implements IUpdater<Map<TK, TV>, TV> {

        public Map<TK, TV> update(Map<TK, TV> orig, IDecrypter<TV> converter) {
            return orig.entrySet().stream().
                    map(entry -> update(entry, converter)).
                    collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        private Map.Entry<TK, TV> update(Map.Entry<TK, TV> entry, IDecrypter<TV> converter) {
            entry.setValue(converter.convertTo(entry.getValue()));
            return entry;
        }
    }

}
