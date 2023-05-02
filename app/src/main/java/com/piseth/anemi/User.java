package com.piseth.anemi;

import android.graphics.Bitmap;

public class User {
    private int id;
    private String username;
    private String password;
    private int userRoleId;
    private String phone;
    private Bitmap photo;
    private int isDeleted;

    public User(int id, String username, String password, int userRoleId, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userRoleId = userRoleId;
        this.phone = phone;
    }

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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
