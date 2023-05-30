package com.piseth.anemi.utils.model;

import androidx.room.Entity;

import com.google.firebase.firestore.Exclude;

@Entity(tableName = "book_detail")
public class Page {
    @Exclude
    private int id;
    private int book_id;
    private String imageURL;

    public Page(){}

    @Exclude
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
