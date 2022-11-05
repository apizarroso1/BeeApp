package com.example.beeapp.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit;
    private static final String BASEURL = "http://localhost:3306/";

    public static Retrofit getRetrofit(){

        if( retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
