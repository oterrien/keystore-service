package com.ote.keystore.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class Page<T> implements Serializable {

    private List<T> content;

    private boolean last;
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;
    private List<Sort> sort;
    private boolean first;
    private int numberOfElements;

    @Data
    public static class Sort implements Serializable {
        private String direction;
        private String property;
        private boolean ignoreCase;
        private String nullHandling;
        private boolean ascending;
        private boolean descending;
    }
}