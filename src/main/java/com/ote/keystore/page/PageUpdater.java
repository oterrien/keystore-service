package com.ote.keystore.page;

import com.ote.keystore.cryptor.annotation.Decrypt;

import java.util.List;
import java.util.stream.Collectors;

public class PageUpdater<T> implements Decrypt.IUpdater<Page<T>, T> {

    public Page<T> update(Page<T> orig, Decrypt.IDecrypter<T> converter) {
        List<T> content = orig.getContent().stream().map(converter::convertTo).collect(Collectors.toList());
        orig.setContent(content);
        return orig;
    }
}