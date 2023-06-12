package com.piseth.anemi.room.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.piseth.anemi.room.dao.UserDao;
import com.piseth.anemi.room.database.AnemiDatabase;
import com.piseth.anemi.utils.model.User;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRoomRepo {
    private UserDao userDao;
    private LiveData<List<User>> userListLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Long id;
    private int row;

    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    public User getUser(long id) {
        return userDao.getUser(id);
    }

    public User getUser(String username) {
        return  userDao.getUser(username);
    }

    public UserRoomRepo(Application application) {
        AnemiDatabase anemiDatabase = AnemiDatabase.getmInstance(application);
        userDao = anemiDatabase.userDao();
        userListLiveData = userDao.getAllUsers();
    }

    public long insertUser(User user) {
        executor.execute(() -> id = userDao.insertUser(user));
        return id;
    }

    public int updateUser(User user) {
        executor.execute(() -> row = userDao.upDateUser(user));
        return row;
    }

    public int deleteUser(long id) {

        executor.execute(() -> row = userDao.deleteUser(id));
        return row;
    }
}
