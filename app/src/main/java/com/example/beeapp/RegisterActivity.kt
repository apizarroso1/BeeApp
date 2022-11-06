package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiInterface
import com.example.beeapp.service.RetrofitService

//import com.google.firebase.database.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level
import java.util.logging.Logger

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerUsername: EditText
    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerRepeatPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var registerGoLoginButton: Button
    private var apiInterface:ApiInterface = RetrofitService().getRetrofit().create()
   /* //private lateinit var auth: FirebaseAuth
    //private lateinit var dbRef: DatabaseReference
    //private lateinit var storage: FirebaseStorage
    //private var comprobacion: Int = 0
    //private val DEFAULT_PROFILE_PICTURE: String = "/images/default_profile.png"
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        /*dbRef =
            Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()*/


        registerUsername = findViewById(R.id.registerUsername)
        registerEmail = findViewById(R.id.registerEmail)
        registerPassword = findViewById(R.id.registerPassword)
        registerRepeatPassword = findViewById(R.id.registerRepeatPassword)
        registerButton = findViewById(R.id.registerRegisterButton)
        registerGoLoginButton = findViewById(R.id.registerGoLoginButton)


        registerButton.setOnClickListener {
            val username = registerUsername.text.toString()
            val email = registerEmail.text.toString()
            val password = registerPassword.text.toString()
            val repeatPassword = registerRepeatPassword.text.toString()

            if (checkEmpty(username, email, password, repeatPassword)) {
                if (password == repeatPassword) {

                   register(username, email, password)


                /*checkUsernameAvailable(username, object : FirebaseCallback {
                        override fun onCallback(success: Boolean) {
                            if (success) {
                                register(username, email, password, success)
                                Toast.makeText(
                                    applicationContext,
                                    "Account successfully created",
                                    Toast.LENGTH_LONG
                                ).show()
                                comprobacion = 1
                            } else if (comprobacion == 0) {
                                Toast.makeText(
                                    applicationContext,
                                    "Username in use",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })*/

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Passwords don't match",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "There are empty fields",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        registerGoLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
    }


    private fun register(username: String, email: String, password: String){


        var user = User(null,username,email,password)

        apiInterface.insertUser(user).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Toast.makeText(
                    applicationContext,
                    "Account successfully created",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Username or email in use",
                    Toast.LENGTH_LONG
                ).show()
                Logger.getLogger("NO IDEA").log(Level.SEVERE, "ERROR",t)
            }
        })




        //Log.e("FUERA DEL BUCLE LLAMADA", "Username already in use $success")


       /* auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    addUserToDataBase(username, email, auth.currentUser?.uid!!)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Register failed", Toast.LENGTH_LONG).show()
                }
            }*/

    }

    /*private interface FirebaseCallback {
        fun onCallback(success: Boolean)
    }*/

    private fun checkUsernameAvailable(username: String){//, firebaseCallback: FirebaseCallback) {
        //var success = true

        /*dbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (currentUser != null) {
                        if (currentUser.username == username) {

                            success = false
                            break

                        }
                    }


                }
                firebaseCallback.onCallback(success)


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "Something went wrong")
            }

        })*/

    }

    private fun checkEmpty(
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
    }

    /* private fun addUserToDataBase(username: String, email: String, uid: String) {
       storage.reference.child(DEFAULT_PROFILE_PICTURE).downloadUrl.addOnSuccessListener {
            dbRef.child("users").child(uid).setValue(User(username, email, uid, it.toString()))
        }

    }*/
}