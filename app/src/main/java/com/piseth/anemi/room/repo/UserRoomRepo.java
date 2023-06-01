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
        final long[] id = new long[1];
        executor.execute(() -> id[0] = userDao.insertUser(user));
        return id[0];
    }

    public int updateUser(User user) {
        final int[] row = new int[1];
        executor.execute(() -> row[0] = userDao.upDateUser(user));
        return row[0];
    }

    public int deleteUser(long id) {
        final int[] row = new int[1];
        executor.execute(() -> row[0] = userDao.deleteUser(id));
        return row[0];
    }
}
