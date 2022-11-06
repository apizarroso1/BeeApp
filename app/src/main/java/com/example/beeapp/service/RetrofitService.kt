package com.example.beeapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    private val BASEURL:String="http://192.168.56.1:8080/beeapp/"
    private val retrofit:Retrofit = initializeRetrofit()

    private fun initializeRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
     fun getRetrofit():Retrofit{
        return retrofit
    }
}