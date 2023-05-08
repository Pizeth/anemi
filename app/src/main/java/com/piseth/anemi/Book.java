package com.piseth.anemi;

import android.graphics.Bitmap;

public class Book {
    private int bookId;
    private String bookName;
    private String description;
    private String author;
    private Bitmap cover;
    private int isDeleted;

    public Book(int bookId, String bookName, String description, String author, Bitmap cover) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.description = description;
        this.author = author;
        this.cover = cover;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
