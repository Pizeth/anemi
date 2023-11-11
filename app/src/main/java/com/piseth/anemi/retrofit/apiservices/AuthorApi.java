package com.piseth.anemi.retrofit.apiservices;

import com.piseth.anemi.utils.model.Author;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthorApi {
    @GET("api/Authors")
    Call<List<Author>> getAll();
    @GET("api/Authors/{id}")
    Call<Author> getById(@Path("id") long id);
    @POST("api/Authors")
    Call<ResponseBody> add(@Body Author author);
    @PUT("api/Authors/{id}")
    Call<ResponseBody> update(@Path("id") long id, @Body Author author);
    @DELETE("api/Authors/{id}")
    Call<ResponseBody> delete(@Path("id") long id);
}
