package com.piseth.anemi.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.piseth.anemi.utils.model.Page;
import com.piseth.anemi.utils.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insertUser(User user);

    @Update
    int upDateUser(User user);

    @Query("UPDATE table_user SET isDeleted = 1 WHERE id = :id")
    int deleteUser(long id);

    @Query("SELECT * FROM table_user WHERE id =:id")
    User getUser(long id);

    @Query("SELECT * FROM table_user WHERE username =:username")
    User getUser(String username);

    @Query("SELECT * FROM table_user")
    LiveData<List<User>> getAllUsers();
}
