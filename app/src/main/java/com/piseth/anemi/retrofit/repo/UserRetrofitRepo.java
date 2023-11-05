package com.piseth.anemi.retrofit.repo;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.piseth.anemi.retrofit.apiservices.UserApi;
import com.piseth.anemi.retrofit.apiservices.UserCallBack;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.RealPathUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRetrofitRepo {
    private Retrofit retrofit;
    private UserApi userApi;
    private static final String TAG = UserRetrofitRepo.class.getSimpleName();
    public UserRetrofitRepo() {
        retrofit = AnemiUtils.getClientApi(HttpLoggingInterceptor.Level.BODY);
        userApi = retrofit.create(UserApi.class);
    }

    public LiveData<List<User>> getAllUsers() {
        final MutableLiveData<List<User>> data = new MutableLiveData<>();
        List<User> allUsers = new ArrayList<>();
        Call<List<User>> call = userApi.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    data.setValue(response.body());
                    Log.d(TAG, "Response body: " + response.body());
                    Log.d(TAG, "Username is: " + response.body().get(0).getUsername());
                    Log.d(TAG, "Email is: " + response.body().get(0).getEmail());
                    Log.d(TAG, "Firstname is: " + response.body().get(0).getFirstName());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "Response code: 500 " + t.getMessage());
            }
        });
        return data;
    }

    public void getUserById(long id, UserCallBack callBack) {
        Call<User> call = userApi.getUserByID(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    callBack.onSuccess(response.body());
                    Log.d(TAG, "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callBack.onFailure();
                Log.d(TAG, "Response code: 500 can't get user" + t.getMessage());
            }
        });
    }

    public void addUser(String path, User user) {
        File image = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        Call<User> call = userApi.addUser(body, user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    Log.d(TAG, "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Response code: 500" + t.getMessage());
            }
        });
    }

    public void updateUser(long id, String path, User user) {
        File image = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        Call<User> call = userApi.updateUser(id, body, user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    Log.d(TAG, "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Response code: 500" + t.getMessage());
            }
        });
    }

    public void deleteUser(long id) {
        Call<User> call = userApi.deleteUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    Log.d(TAG, "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Response code: 500" + t.getMessage());
            }
        });
    }
}
