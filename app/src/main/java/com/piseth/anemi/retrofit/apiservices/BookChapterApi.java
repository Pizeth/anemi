package com.piseth.anemi.retrofit.apiservices;

import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.BookChapter;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookChapterApi {
    @GET("api/BookChapters")
    Call<List<BookChapter>> getBookChapters();
    @GET("api/BookChapters/{id}")
    Call<BookChapter> getBookChapterByID(@Path("id") long id);
    @POST("api/BookChapters")
    Call<ResponseBody> addBookChapter(@Body BookChapter bookChapter);
    @PUT("api/BookChapters/{id}")
    Call<ResponseBody> updateBookChapter(@Path("id") long id, @Body BookChapter bookChapter);
    @DELETE("api/BookChapters/{id}")
    Call<ResponseBody> deleteUBookChapter(@Path("id") long id);
}
