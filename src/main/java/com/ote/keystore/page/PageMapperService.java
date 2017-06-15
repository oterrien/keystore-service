package com.ote.keystore.page;

import com.ote.keystore.page.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PageMapperService {

    public <T> Page<T> convertTo(org.springframework.data.domain.Page<T> springPage) {

        Page<T> page = new Page<>();

        page.setContent(springPage.getContent());
        page.setLast(springPage.isLast());
        page.setTotalElements(springPage.getTotalElements());
        page.setTotalPages(springPage.getTotalPages());
        page.setSize(springPage.getSize());
        page.setNumber(springPage.getNumber());

        if (springPage.getSort() != null) {
            page.setSort(StreamSupport.stream(springPage.getSort().spliterator(), false).map(this::convertToSort).collect(Collectors.toList()));
        }

        page.setFirst(springPage.isFirst());
        page.setNumberOfElements(springPage.getNumberOfElements());

        return page;

    }

    private Page.Sort convertToSort(org.springframework.data.domain.Sort.Order springOrder) {

        Page.Sort sort = new Page.Sort();
        sort.setDirection(springOrder.getDirection().toString());
        sort.setProperty(springOrder.getProperty());
        sort.setIgnoreCase(springOrder.isIgnoreCase());
        sort.setNullHandling(springOrder.getNullHandling().name());
        sort.setAscending(springOrder.isAscending());
        sort.setDescending(springOrder.isDescending());
        return sort;
    }
}
