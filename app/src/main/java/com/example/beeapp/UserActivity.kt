package com.example.beeapp


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

import com.example.beeapp.databinding.ActivityUserBinding
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UserActivity : AppCompatActivity() {
    private lateinit var ivProfilePicture: ImageView
    private lateinit var viewBinding: ActivityUserBinding
    private lateinit var tvUsername:TextView
    private lateinit var tvEmail:TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var username:String
    private lateinit var email:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        username= intent.getStringExtra("username").toString()
        email= intent.getStringExtra("email").toString()
        initView()
        initListeners()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    fun initView(){

        ivProfilePicture = viewBinding.ivProfilePicture
        tvUsername = viewBinding.textView2
        tvEmail = viewBinding.textView3
        tvUsername.text = "Username: $username"
        tvEmail.text = "Email: $email"

        var imageRef: String? = null
        dbRef =
            Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()

        dbRef.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (postSnapshot in snapshot.children) {
                        val currentUser = postSnapshot.getValue(User::class.java)

                        if (auth.currentUser?.uid.equals(currentUser?.uid)) {
                            //imageRef = currentUser?.profilePicture
                        }
                        try {
                            Glide.with(this@UserActivity).load(imageRef).into(ivProfilePicture)

                        }catch (e:Exception){
                            e.stackTrace
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }

    fun initListeners(){
        ivProfilePicture.setOnClickListener{editProfilePicture()}

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

                    dbRef.child("users").child(auth.currentUser?.uid.toString()).child("profilePicture").setValue(it.toString())
                }
            }
    }


}