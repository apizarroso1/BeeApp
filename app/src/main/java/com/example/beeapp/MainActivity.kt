package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.beeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var goUserButton: ImageView
    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        initView()
        initListeners()
    }

    private fun initView(){
        goUserButton = viewBinding.mainGoUserButton
    }

    private fun initListeners(){
        goUserButton.setOnClickListener { displayUser() }
    }

    private fun displayUser(){
        //startActivity(Intent(this, UserActivity::class.java))
        //finish()
        Toast.makeText(this, "SI va", Toast.LENGTH_LONG).show()

    }
}