package com.piseth.anemi.retrofit.apiservices;

import com.piseth.anemi.utils.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi {
    @GET("api/Users")
    Call<List<User>> getUsers();
    @GET("api/Users/{id}")
    Call<User> getUserByID(@Path("id") long id);
    @Multipart
    @POST("api/Users/Register")
    Call<User> addUser(@Part MultipartBody.Part image,
                       @Part("user") User user);
    @PUT("api/Users/{id}")
    Call<User> updateUser(@Path("id") long id,
//                          @Part MultipartBody.Part image,
                          @Body User user);
    @DELETE("api/Users/{id}")
    Call<User> deleteUser(@Path("id") long id);
}
