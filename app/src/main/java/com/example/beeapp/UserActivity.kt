package com.example.beeapp


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore

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
        ivProfilePicture.setOnClickListener{
            editProfilePicture()}
        ivEditUsername.setOnClickListener{editUsername()}

    }

    fun editProfilePicture(){

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null)
        {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            ivProfilePicture.setBackgroundDrawable(bitmapDrawable)

        }
    }

    fun editUsername(){

    }
}