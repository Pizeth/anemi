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

    public UserRoomRepo(Application application) {
        AnemiDatabase anemiDatabase = AnemiDatabase.getmInstance(application);
        userDao = anemiDatabase.uSerDao();
        userListLiveData = userDao.getAllUsers();
    }

    public void insertUser(User user) {
        executor.execute(() -> userDao.insertPage(user));
    }
}
