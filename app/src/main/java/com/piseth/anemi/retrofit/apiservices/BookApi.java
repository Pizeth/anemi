package com.piseth.anemi.retrofit.apiservices;

import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.TokenUser;
import com.piseth.anemi.utils.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookApi {
    @GET("api/Books")
    Call<List<Book>> getBooks();
    @GET("api/Books/{id}")
    Call<Book> getBookByID(@Path("id") long id);
    @POST("api/Books")
    Call<ResponseBody> addBook(@Body Book book);
    @PUT("api/Books/{id}")
    Call<ResponseBody> updateBook(@Path("id") long id, @Body Book book);
    @DELETE("api/Books/{id}")
    Call<ResponseBody> deleteUBook(@Path("id") long id);
}
