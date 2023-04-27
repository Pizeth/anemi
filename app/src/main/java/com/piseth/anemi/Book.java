package com.piseth.anemi;

public class Book {
    private int bookId;
    private String bookName;
    private String description;
    private String author;
    private int page;
    private int isDeleted;

    public Book(int bookId, String bookName, String description, String author, int page) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.description = description;
        this.author = author;
        this.page = page;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
