package com.piseth.anemi.service;

import com.piseth.anemi.utils.model.Book;

import java.util.List;

public interface IBookLoadDone {
    void onBookLoadDoneListener(List<Book> books);
}
