package com.example.beeapp.service;

import com.example.beeapp.model.Chat;
import com.example.beeapp.model.Message;
import com.example.beeapp.model.User;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiChatInterface {
    @GET("chat/consult")
    Call<List<Chat>> getChats();

    @DELETE("chat/delete/{id}")
    Call<String> deleteChat(@Path("id") String id);

    @GET("chat/consult/{id}")
    Call<Chat> getChatById(@Path("id") String id);

    @GET("chat/findalleventchats")
    Call<List<Chat>> findAllEventChatsFromUser(@Query("userId") String userId);

    @GET("chat/findchat")
    Call<Chat> findChat(@Query("id1") String id1,@Query("id2") String id2);

    @GET("chat/getmessages")
    Call<Set<Message>> getMessages(@Query("chat") Chat chat);

    @POST("chat/insert")
    Call<Chat> insertChat(@Body Chat chat);

    @PUT("chat/update")
    Call<Chat> updateChat(@Body Chat chat);

}
