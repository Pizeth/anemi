package com.piseth.anemi.retrofit.repo;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.piseth.anemi.retrofit.apiservices.BookApi;
import com.piseth.anemi.retrofit.apiservices.UserCallBack;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.TokenUser;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;

import java.util.List;

import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookRetrofitRepo {
    private Retrofit retrofit;
    private BookApi bookApi;
    private static final String TAG = BookRetrofitRepo.class.getSimpleName();
    public BookRetrofitRepo() {
        retrofit = AnemiUtils.getClientApi(HttpLoggingInterceptor.Level.BODY);
        bookApi = retrofit.create(BookApi.class);
    }

    public LiveData<List<Book>> getAllBooks() {
        final MutableLiveData<List<Book>> data = new MutableLiveData<>();
        Call<List<Book>> call = bookApi.getBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body());
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d(TAG, "Response code: 500 " + t.getMessage());
            }
        });
        return data;
    }

    public LiveData<Book> getBookById(long id) {
        final MutableLiveData<Book> book = new MutableLiveData<>();
        Call<Book> call = bookApi.getBookByID(id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "Response code: " + response.code());
                    return;
                }
                if (response.body() != null) {
                    book.setValue(response.body());
                    Log.d(TAG, "Response body: " + response.body());
                    Log.d(TAG, "Username is: " + response.body().getBookTitle());
                    Log.d(TAG, "Email is: " + response.body().getDescription());
                    Log.d(TAG, "Firstname is: " + response.body().getCover());
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d(TAG, "Response code: 500 " + t.getMessage());
            }
        });
        return book;
    }

    public void addBook(Book book) {
        Call<ResponseBody> call = bookApi.addBook(book);
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

    public void updateBook(long id, Book book) {
        Call<ResponseBody> call = bookApi.updateBook(id, book);
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

    public void deleteBook(long id) {
        Call<ResponseBody> call = bookApi.deleteUBook(id);
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
