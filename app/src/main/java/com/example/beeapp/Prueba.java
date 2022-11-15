package com.example.beeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.beeapp.service.ApiUserInterface;

public class Prueba extends AppCompatActivity {

    ApiUserInterface apiUserInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);



    }
}