package com.piseth.anemi.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.piseth.anemi.room.repo.UserRoomRepo;
import com.piseth.anemi.utils.model.User;

import java.util.List;

public class UserRoomViewModel extends AndroidViewModel {
    private UserRoomRepo userRoomRepo;

    public UserRoomViewModel(@NonNull Application application) {
        super(application);
        userRoomRepo = new UserRoomRepo(application);
    }

    public long insertUser(User user) {
        return userRoomRepo.insertUser(user);
    }

    public int updateUser(User user) {
        return userRoomRepo.updateUser(user);
    }

    public int deleteUser(long id) {
        return userRoomRepo.deleteUser(id);
    }

    public User getUser(long id) {
        return userRoomRepo.getUser(id);
    }

    public User getUser(String username) {
        return userRoomRepo.getUser(username);
    }
    public LiveData<List<User>> getAllUsersLiveData() {
        return userRoomRepo.getUserListLiveData();
    }
}
