package com.piseth.anemi.retrofit.repo;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.piseth.anemi.retrofit.apiservices.UserApi;
import com.piseth.anemi.retrofit.apiservices.UserCallBack;
import com.piseth.anemi.utils.model.TokenUser;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.RealPathUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
//                    Log.d(TAG, "Response body: " + response.body());
//                    Log.d(TAG, "Username is: " + response.body().get(0).getUsername());
//                    Log.d(TAG, "Email is: " + response.body().get(0).getEmail());
//                    Log.d(TAG, "Firstname is: " + response.body().get(0).getFirstName());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "Response code: 500 " + t.getMessage());
            }
        });
        return data;
    }

    public LiveData<User> getUserById(long id) {
        final MutableLiveData<User> user = new MutableLiveData<>();
        Call<User> call = userApi.getUserByID(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    user.setValue(response.body());
                    Log.d(TAG, "Response body: " + response.body());
                    Log.d(TAG, "Username is: " + response.body().getUsername());
                    Log.d(TAG, "Email is: " + response.body().getEmail());
                    Log.d(TAG, "Firstname is: " + response.body().getFirstName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Response code: 500 " + t.getMessage());
            }
        });
        return user;
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

    public LiveData<TokenUser> login(User user) {
        final MutableLiveData<TokenUser> loggedInUser = new MutableLiveData<>();
        Call<TokenUser> call = userApi.login(user);
        call.enqueue(new Callback<TokenUser>() {
            @Override
            public void onResponse(Call<TokenUser> call, Response<TokenUser> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    Log.d(TAG, "Response body: " + response.body());
                    loggedInUser.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<TokenUser> call, Throwable t) {
                Log.d(TAG, "Response code: 500" + t.getMessage());
            }
        });
        return loggedInUser;
    }

    public void registerUser(User user, SharedPreferences loggedInUser) {
//        File image = new File(path);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        final MutableLiveData<TokenUser> tokenUser = new MutableLiveData<>();
        Call<TokenUser> call = userApi.registerUser(user);
        call.enqueue(new Callback<TokenUser>() {
            @Override
            public void onResponse(Call<TokenUser> call, Response<TokenUser> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    Log.d(TAG, "Response body: " + response.body());
                    AnemiUtils.setUserPreference(loggedInUser, response.body());
                }
            }

            @Override
            public void onFailure(Call<TokenUser> call, Throwable t) {
                Log.d(TAG, "Response code: 500" + t.getMessage());
            }
        });
    }

    public void updateUser(long id, User user) {
//        File image = new File(path);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        Call<User> call = userApi.updateUser(id, /*body,*/ user);
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
        Call<ResponseBody> call = userApi.deleteUser(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    Log.d(TAG, "Response body: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Response code: 500" + t.getMessage());
            }
        });
    }
}
