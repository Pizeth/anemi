package com.piseth.anemi.utils.model;

import java.util.ArrayList;
import java.util.List;

public class BookChapter {
    private int id;
    private int bookId;
    private String chapterTitle;
    private String cover;
    private Book book;
    private List<ChapterDetail> chapterDetail;

    public BookChapter() {
        chapterDetail = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<ChapterDetail> getChapterDetail() {
        return chapterDetail;
    }

    public void setChapterDetail(List<ChapterDetail> chapterDetail) {
        this.chapterDetail = chapterDetail;
    }
}
