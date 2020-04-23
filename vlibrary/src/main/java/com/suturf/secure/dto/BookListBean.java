package com.suturf.secure.dto;

import io.micronaut.core.annotation.Introspected;

import java.util.ArrayList;
import java.util.List;

@Introspected
public class BookListBean {
    private boolean retCode;
    private List<BookBean> books;

    public BookListBean() {
        books = new ArrayList<>();
    }

    public boolean isRetCode() {
        return retCode;
    }

    public void setRetCode(boolean retCode) {
        this.retCode = retCode;
    }

    public List<BookBean> getBooks() {
        return books;
    }

    public void setBooks(List<BookBean> books) {
        this.books = books;
    }
}
