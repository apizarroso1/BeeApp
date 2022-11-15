package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger


class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var loginGoRegisterButton: Button
 //   private lateinit var auth: FirebaseAuth
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()

    companion object {
        lateinit var loggedUser: User
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
 //       auth = FirebaseAuth.getInstance()

        checkLoggedUser()

        loginEmail = findViewById(R.id.loginEmail)
        loginPassword = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginLoginButton)
        loginGoRegisterButton = findViewById(R.id.loginGoRegisterButton)

        loginButton.setOnClickListener {
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            if (checkEmpty(email, password)) {
                login(email, password)
            } else {
                Toast.makeText(applicationContext, "There are empty fields", Toast.LENGTH_LONG)
                    .show()
            }
        }

        loginGoRegisterButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()

        }

    }

    private fun checkLoggedUser(){


        /*if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }*/
    }
//Funcion login hace una consulta con el email indicado
    private fun login(email: String, password: String) {

        apiUserInterface.getUserByEmail(email).enqueue(object: Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {



                if (response.code()!=200){
                    Toast.makeText(
                        applicationContext,
                        "User not found",
                        Toast.LENGTH_LONG
                    ).show()
                    Logger.getLogger("User not found").log(Level.WARNING, "code=${response.code()}")
                }else{

                    if(response.body()!!.password.equals(password)){

                        Toast.makeText(
                            applicationContext,
                            "Logged ",
                            Toast.LENGTH_LONG
                        ).show()
                        loggedUser = response.body()!!
                        Logger.getLogger("USER LOGGED").log(Level.INFO, "${response.body()}")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "Incorrect password ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
                Logger.getLogger("ERROR").log(Level.SEVERE, "Unknown ERROR trying to loggin",t)
            }


        })

    }

    private fun checkEmpty(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }




}