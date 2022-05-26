package com.example.beeapp


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast

import com.example.beeapp.databinding.ActivityUserBinding
import com.google.firebase.storage.FirebaseStorage
import java.util.*

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

    var selectedImage: Uri? = null

    fun editProfilePicture(){

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent, 3)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null)
        {
            selectedImage = data.data
            ivProfilePicture.setImageURI(selectedImage)


            upploadImage()
        }

    }

    private fun upploadImage(){
        val filename = UUID.randomUUID().toString()
        val ref =FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedImage!!)
            .addOnSuccessListener {
                Toast.makeText(this,"Foto guardada",Toast.LENGTH_LONG).show()
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()

                }
            }


    }

    fun editUsername(){

    }
}