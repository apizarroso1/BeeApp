package com.example.beeapp.service;

import com.example.beeapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("user/consult")
    Call<List<User>> getUsers();

    @POST("user/insert")
    Call<User> insertUser(@Body User user);

}
