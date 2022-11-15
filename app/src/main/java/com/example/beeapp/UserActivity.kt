package com.example.beeapp


import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.beeapp.LoginActivity.Companion.loggedUser

import com.example.beeapp.databinding.ActivityUserBinding
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class UserActivity : AppCompatActivity() {
    private lateinit var ivProfilePicture: ImageView
    private lateinit var viewBinding: ActivityUserBinding
    private lateinit var tvUsername:TextView
    private lateinit var tvMood:TextView
   // private lateinit var auth: FirebaseAuth
   // private lateinit var dbRef: DatabaseReference
    private lateinit var username:String
    private var mood:String = ""
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private lateinit var dialog: AlertDialog
    private lateinit var editMood: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        username=loggedUser.username //intent.getStringExtra("username").toString()
        //email= intent.getStringExtra("email").toString()

        initView()
        initData()
        initListeners()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun initData(){



        mood = loggedUser.mood
        tvMood.text = "$mood"
        dialog = AlertDialog.Builder(this).create()
        dialog.setTitle("Mood")
        editMood = EditText(this)
        setEditMoodButton()
        dialog.setView(editMood)


    }

    private fun setEditMoodButton() {
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"Change Mood"){ dialogInterface, i ->
            tvMood.text = editMood.text
            loggedUser.mood = editMood.text.toString()

            var updatedUser= User(loggedUser)

            apiUserInterface.updateUser(updatedUser).enqueue(object:Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code()==202){
                        Toast.makeText(
                            applicationContext,
                            "Mood changed",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("Mood changed").log(Level.SEVERE, "code:${response.code()}")
                    }else {
                        Toast.makeText(
                            applicationContext,
                            "Couldn't change the mood, response code:${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("Couldn't change the mood").log(Level.SEVERE, "Couldn't change the mood, response code:${response.code()}")


                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR trying to change the mood").log(Level.SEVERE, "Unknown ERROR",t)
                }
            })

        }
    }


    private fun initView(){

        ivProfilePicture = viewBinding.ivProfilePicture
        tvUsername = viewBinding.tvUsername
        tvMood = viewBinding.tvMood
        tvUsername.text = "$username"
        tvMood.text = "$mood"

        var imageRef: String? = null
        //dbRef =
       //     Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
       // auth = FirebaseAuth.getInstance()

       /* dbRef.child("users")
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
            })*/

    }

    private fun initListeners(){
        ivProfilePicture.setOnClickListener{editProfilePicture()}
        tvMood.setOnClickListener {

            editMood.setText(tvMood.text)
            dialog.show()}

    }

    var selectedImage: Uri? = null

    private fun editProfilePicture(){

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent, 3)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null)
        {
            selectedImage = data.data
            ivProfilePicture.setImageURI(selectedImage)
            uploadImage()
        }

    }

    private fun uploadImage(){
        val filename = UUID.randomUUID().toString()
      //  val ref =FirebaseStorage.getInstance().getReference("/images/$filename")

       /* ref.putFile(selectedImage!!)
            .addOnSuccessListener {
                Toast.makeText(this,"Foto guardada",Toast.LENGTH_LONG).show()
                ref.downloadUrl.addOnSuccessListener {

                   // dbRef.child("users").child(auth.currentUser?.uid.toString()).child("profilePicture").setValue(it.toString())
                }
            }*/
    }


}