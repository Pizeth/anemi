package com.piseth.anemi.utils.model;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;

@Entity(tableName = "table_user")
public class User {
    @Exclude
    @PrimaryKey
    private int id;
    private String username;
    private String email;
    private String password;
    private int userRoleId;
    private String phone;
    private String photo;
    @ColumnInfo(defaultValue = "0")
    private int isDeleted;

//    public User(int id, String username, String password, int userRoleId, String phone, Bitmap photo) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.userRoleId = userRoleId;
//        this.phone = phone;
//        this.photo = photo;
//    }
    public User() {}
    public User(String username, String email, String password, int userRoleId, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRoleId = userRoleId;
        this.phone = phone;
    }

    public User(int id, String username, String password, int userRoleId, String phone, String photo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userRoleId = userRoleId;
        this.phone = phone;
        this.photo = photo;
    }

    @Exclude
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public Bitmap getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(Bitmap photo) {
//        this.photo = photo;
//    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
