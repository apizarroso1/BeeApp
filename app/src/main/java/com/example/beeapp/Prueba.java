package com.example.beeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.beeapp.model.User;
import com.example.beeapp.service.ApiInterface;
import com.example.beeapp.service.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Prueba extends AppCompatActivity {

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);


            apiInterface = RetrofitService.getRetrofit().create(ApiInterface.class);

            apiInterface.getUsers().enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                    if (response.body().size()>0){
                        Log.d("si va","hay cosas");
                    }else{
                        Log.d("si va","no hay cosas");
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.d("Nada","nada");
                }
            });

    }
}