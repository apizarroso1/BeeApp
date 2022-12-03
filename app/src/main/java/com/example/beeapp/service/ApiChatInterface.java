package com.example.beeapp.service;

import com.example.beeapp.model.Chat;
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

public interface ApiChatInterface {
    @GET("chat/consult")
    Call<List<Chat>> getChats();

    @GET("chat/consult/{id}")
    Call<Chat> getChatById(@Path("id") String id);

    @GET("chat/findallchats")
    Call<List<Chat>> findAllChatsFromUser(@Query("userId") String userId);

    @POST("chat/insert")
    Call<Chat> insertChat(@Body Chat chat);

    @PUT("chat/update")
    Call<Chat> updateChat(@Body Chat chat);
}
