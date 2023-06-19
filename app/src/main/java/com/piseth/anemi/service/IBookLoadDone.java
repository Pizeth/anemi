package com.piseth.anemi.service;

import com.piseth.anemi.utils.model.Book;

import java.util.List;

//Future Plan for loading banner
public interface IBookLoadDone {
    void onBookLoadDoneListener(List<Book> books);
}
