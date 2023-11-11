package com.piseth.anemi.retrofit.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.piseth.anemi.retrofit.apiservices.AuthorApi;
import com.piseth.anemi.utils.model.Author;
import com.piseth.anemi.utils.util.AnemiUtils;

import java.util.List;

import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthorRetrofitRepo {
    private Retrofit retrofit;
    private AuthorApi authorApi;
    private static final String TAG = AuthorRetrofitRepo.class.getSimpleName();
    public AuthorRetrofitRepo() {
        retrofit = AnemiUtils.getClientApi(HttpLoggingInterceptor.Level.BODY);
        authorApi = retrofit.create(AuthorApi.class);
    }

    public LiveData<List<Author>> getAll() {
        final MutableLiveData<List<Author>> data = new MutableLiveData<>();
        Call<List<Author>> call = authorApi.getAll();
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                Log.d(TAG, "Response code: 500 " + t.getMessage());
            }
        });
        return data;
    }

    public LiveData<Author> getById(long id) {
        final MutableLiveData<Author> author = new MutableLiveData<>();
        Call<Author> call = authorApi.getById(id);
        call.enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    author.setValue(response.body());
                    Log.d(TAG, "Response body: " + response.body());
                    Log.d(TAG, "Username is: " + response.body().getPenName());
                    Log.d(TAG, "Email is: " + response.body().getAvatar());
                }
            }

            @Override
            public void onFailure(Call<Author> call, Throwable t) {
                Log.d(TAG, "Response code: 500 " + t.getMessage());
            }
        });
        return author;
    }

    public void add(Author author) {
        Call<ResponseBody> call = authorApi.add(author);
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

    public void update(long id, Author author) {
        Call<ResponseBody> call = authorApi.update(id, author);
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

    public void delete(long id) {
        Call<ResponseBody> call = authorApi.delete(id);
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
