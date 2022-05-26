package com.example.beeapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.ImageView

import com.example.beeapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var ivProfilePicture: ImageView
    private lateinit var ivEditUsername: ImageView
    private lateinit var viewBinding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        viewBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        initView()
        initListeners()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun initView(){
        ivProfilePicture = viewBinding.ivProfilePicture
        ivEditUsername = viewBinding.ivEditUsername

    }

    fun initListeners(){
        ivProfilePicture.setOnClickListener{editProfilePicture()}
        ivEditUsername.setOnClickListener{editUsername()}

    }

    fun editProfilePicture(){

    }

    fun editUsername(){

    }
}