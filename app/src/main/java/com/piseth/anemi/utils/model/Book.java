package com.piseth.anemi.utils.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;

@Entity(tableName = "table_book")
public class Book {
    @Exclude
    @PrimaryKey
    private int bookId;
    private String bookName;
    private String description;
    private String author;
    private String cover;
    @ColumnInfo(defaultValue = "0")
    private int isDeleted;

    public Book() {}

    public Book(String bookName, String description, String author) {
        this.bookName = bookName;
        this.description = description;
        this.author = author;
    }
    public Book(int bookId, String bookName, String description, String author, String cover) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.description = description;
        this.author = author;
        this.cover = cover;
    }

    @Exclude
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

//    public Bitmap getCover() {
//        return cover;
//    }
//
//    public void setCover(Bitmap cover) {
//        this.cover = cover;
//    }
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
