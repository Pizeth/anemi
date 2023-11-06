package com.piseth.anemi.retrofit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.piseth.anemi.retrofit.apiservices.UserCallBack;
import com.piseth.anemi.retrofit.repo.UserRetrofitRepo;
import com.piseth.anemi.utils.model.User;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {
    private UserRetrofitRepo userRetrofitRepo;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRetrofitRepo = new UserRetrofitRepo();
    }

    public LiveData<List<User>> getAllUsers() {
        return userRetrofitRepo.getAllUsers();
    }
    public void getUserById(long id, UserCallBack callBack) { userRetrofitRepo.getUserById(id, callBack); }
//    public void addUser(String path, User user) { userRetrofitRepo.addUser(path, user); }
    public void addUser(File file, User user) { userRetrofitRepo.addUser(file, user); }
//    public void updateUser(long id, String path, User user) { userRetrofitRepo.updateUser(id, path, user); }
    public void updateUser(long id, File file, User user) { userRetrofitRepo.updateUser(id, file, user); }
    public void deleteUser(long id) { userRetrofitRepo.deleteUser(id); }
}
