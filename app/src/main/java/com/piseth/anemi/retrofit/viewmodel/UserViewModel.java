package com.piseth.anemi.retrofit.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.piseth.anemi.retrofit.apiservices.UserCallBack;
import com.piseth.anemi.retrofit.repo.UserRetrofitRepo;
import com.piseth.anemi.utils.model.TokenUser;
import com.piseth.anemi.utils.model.User;

import java.io.File;
import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRetrofitRepo userRetrofitRepo;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRetrofitRepo = new UserRetrofitRepo();
        allUsers = userRetrofitRepo.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    public LiveData<User> getUserById(long id) { return userRetrofitRepo.getUserById(id); }
    public void getUserById(long id, UserCallBack callBack) { userRetrofitRepo.getUserById(id, callBack); }
    public LiveData<TokenUser> login(User user) { return userRetrofitRepo.login(user); }
    public void registerUser(User user, SharedPreferences loggedInUser) { userRetrofitRepo.registerUser(user, loggedInUser); }
//    public void updateUser(long id, String path, User user) { userRetrofitRepo.updateUser(id, path, user); }
    public void updateUser(long id, User user) { userRetrofitRepo.updateUser(id, user); }
    public void deleteUser(long id) { userRetrofitRepo.deleteUser(id); }
}
