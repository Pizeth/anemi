package com.piseth.anemi.utils.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

//@Entity(tableName = "table_book")
public class Book {
//    @Exclude
//    @PrimaryKey
    private int bookId;
    private String bookTitle;
    private String description;
    private int authorId;
    private String publishedDate;
    private int categoryId;
    private int genreId;
    private List<BookChapter> bookChapters;
    private String cover;
    private Author author;
    private Genre genre;
    private Category category;
//    @ColumnInfo(defaultValue = "0")
//    private int isDeleted;

    public Book() {
        bookChapters = new ArrayList<>();
    }

    public Book(String bookTitle, String description, String author) {
        this.bookTitle = bookTitle;
        this.description = description;
    }
    public Book(int bookId, String bookTitle, String description, String author, String cover) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.description = description;
        this.cover = cover;
    }

//    @Exclude
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishDate) {
        this.publishedDate = publishDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public List<BookChapter> getBookChapters() {
        return bookChapters;
    }

    public void setBookChapters(List<BookChapter> bookChapters) {
        this.bookChapters = bookChapters;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
