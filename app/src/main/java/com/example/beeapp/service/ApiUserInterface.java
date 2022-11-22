package com.example.beeapp.service;

import com.example.beeapp.model.User;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiUserInterface {


    @GET("user/consult")
    Call<List<User>> getUsers();

    @GET("user/consultbyusername")
    Call<User> getUserByUsername(@Query("username") String username);

    @GET("user/consultbyemail")
    Call<User> getUserByEmail(@Query("email") String email);

    @GET("user/consult/{id}")
    Call<User> getUserById(@Path("id") String id);

    @GET("user/searchusers")
    Call<List<User>> searchUser(@Query("username") String username);

    @GET("user/getcontacts")
    Call<List<User>> findContacts(@Query("userIds")Set<String> userIds);

    @POST("user/insert")
    Call<User> insertUser(@Body User user);

    @PUT("user/update")
    Call<User> updateUser(@Body User user);


}
