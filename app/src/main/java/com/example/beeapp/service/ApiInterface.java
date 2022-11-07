package com.example.beeapp.service;

import com.example.beeapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("user/consult")
    Call<List<User>> getUsers();

    @GET("user/consultbyusername")
    Call<User> getUserByUsername(@Body String username);

    @GET("user/consultbyemail")
    Call<User> getUserByEmail(@Query("email") String email);

    @POST("user/insert")
    Call<User> insertUser(@Body User user);

}
