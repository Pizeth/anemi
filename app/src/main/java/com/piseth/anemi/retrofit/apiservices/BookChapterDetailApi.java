package com.piseth.anemi.retrofit.apiservices;

import com.piseth.anemi.utils.model.BookChapter;
import com.piseth.anemi.utils.model.ChapterDetail;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookChapterDetailApi {
    @GET("api/ChapterDetails")
    Call<List<ChapterDetail>> getBookChapters();
    @GET("api/ChapterDetails/{id}")
    Call<ChapterDetail> getBookChapterByID(@Path("id") long id);
    @POST("api/ChapterDetails")
    Call<ResponseBody> addBookChapter(@Body ChapterDetail chapterDetail);
    @PUT("api/ChapterDetails/{id}")
    Call<ResponseBody> updateBookChapter(@Path("id") long id, @Body ChapterDetail chapterDetail);
    @DELETE("api/ChapterDetails/{id}")
    Call<ResponseBody> deleteUBookChapter(@Path("id") long id);
}
