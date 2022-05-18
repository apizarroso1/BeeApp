package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var goUserButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initListeners()
    }

    private fun initView(){
        goUserButton = findViewById(R.id.mainGoUserButton)
    }

    private fun initListeners(){
        goUserButton.setOnClickListener { displayUser() }
    }

    private fun displayUser(){
        //startActivity(Intent(this, UserActivity::class.java))
        finish()
    }
}