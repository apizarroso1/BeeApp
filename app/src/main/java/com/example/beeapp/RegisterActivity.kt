package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerUsername: EditText
    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerRepeatPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var registerGoLoginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private var success: Boolean = true

    private val DEFAULT_PROFILE_PICTURE: String = "/images/default_profile.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        dbRef = Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()


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

            if (checkEmpty(username,email, password, repeatPassword)){
                if (password == repeatPassword){
                    register(username,email, password)
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

        registerGoLoginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
    }

    private fun register(username:String, email: String, password: String) {

       if (checkUsernameAvailable(username)){
           auth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(this) { task ->
                   if (task.isSuccessful) {
                       Toast.makeText(
                           applicationContext,
                           "Account successfully created",
                           Toast.LENGTH_LONG
                       ).show()
                       addUserToDataBase(username,email, auth.currentUser?.uid!!)
                       startActivity(Intent(this, MainActivity::class.java))
                       finish()
                   } else {
                       Toast.makeText(applicationContext, "Register failed", Toast.LENGTH_LONG).show()
                   }
               }
       } else {
           Toast.makeText(applicationContext, "Register failed", Toast.LENGTH_LONG).show()
       }
    }

    private fun checkUsernameAvailable(username: String): Boolean{
        dbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (currentUser != null) {
                        if (currentUser.username == username){
                            Toast.makeText(applicationContext, "Username already in use " + success.toString(), Toast.LENGTH_LONG).show()
                            success = false
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        Toast.makeText(applicationContext, success.toString() , Toast.LENGTH_LONG).show()
        return success

        TODO("No sigo perdiendo el tiempo en esto que seguramente puedas solucionar tú en nada: el valor de la variable vuelve a true al salir del metodo")
    }

    private fun checkEmpty(username:String, email: String, password: String, repeatPassword: String): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
    }

    private fun addUserToDataBase(username: String, email: String, uid: String) {
        storage.reference.child(DEFAULT_PROFILE_PICTURE).downloadUrl.addOnSuccessListener {
            dbRef.child("users").child(uid).setValue(User(username,email,uid,it.toString()))
        }

    }
}