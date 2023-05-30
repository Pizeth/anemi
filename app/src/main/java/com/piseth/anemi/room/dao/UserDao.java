package com.piseth.anemi.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import com.piseth.anemi.utils.model.Page;
import com.piseth.anemi.utils.model.User;

import java.util.List;

public interface UserDao {
    @Insert
    void insertPage(User user);

    @Query("SELECT * FROM table_user WHERE id =:id")
    User getUser(long id);

    @Query("SELECT * FROM table_user WHERE username =:username")
    User getUser(String username);

    @Query("SELECT * FROM table_user")
    LiveData<List<User>> getAllUsers();
}
