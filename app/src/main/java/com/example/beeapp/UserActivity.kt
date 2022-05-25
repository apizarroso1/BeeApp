package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.beeapp.databinding.ActivityChatBinding
import com.example.beeapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var ivProfilePicture: ImageView
    private lateinit var ivEditUsername: ImageView
    private lateinit var btnGoBack: Button
    private lateinit var viewBinding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        viewBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initView()
        initListeners()
    }

    fun initView(){
        ivProfilePicture = viewBinding.ivProfilePicture
        ivEditUsername = viewBinding.ivEditUsername
        btnGoBack = viewBinding.btnGoBackUser
    }

    fun initListeners(){
        ivProfilePicture.setOnClickListener{editProfilePicture()}
        ivEditUsername.setOnClickListener{editUsername()}
        btnGoBack.setOnClickListener { startActivity(Intent(this, MainActivity::class.java))
            finish()}
    }

    fun editProfilePicture(){

    }

    fun editUsername(){

    }
}