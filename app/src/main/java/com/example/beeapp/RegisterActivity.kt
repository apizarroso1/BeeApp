package com.example.beeapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService

//import com.google.firebase.database.*
import retrofit2.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerUsername: EditText
    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerRepeatPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var registerGoLoginButton: Button
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        registerUsername = findViewById(R.id.registerUsername)
        registerEmail = findViewById(R.id.registerEmail)
        registerPassword = findViewById(R.id.registerPassword)
        registerRepeatPassword = findViewById(R.id.registerRepeatPassword)
        registerButton = findViewById(R.id.registerRegisterButton)
        registerGoLoginButton = findViewById(R.id.registerGoLoginButton)

        initListeners()
    }
    //Función que se encarga de inicializar los listeners
    private fun initListeners() {

        registerButton.setOnClickListener {
            val username = registerUsername.text.toString()
            val email = registerEmail.text.toString()
            val password = registerPassword.text.toString()
            val repeatPassword = registerRepeatPassword.text.toString()

            if (checkEmpty(username, email, password, repeatPassword)) {

                if (checkEmailValid(email)) {
                    if (password == repeatPassword) {

                        register(username, email, password)

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
                        "Invalid Email",
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
    //Función que se encarga de crear la imagen por defecto del usuario
    private fun setDefaultPicture(): ByteArray {


        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.defaultbee)
        var byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        var bytes = byteArrayOutputStream.toByteArray()


        return bytes
    }
    //Función que se encarga de crear un objeto de la clase usuario y meterlo en la base de datos
    private fun register(username: String, email: String, password: String) {

        var id = UUID.randomUUID().toString()
        Logger.getLogger("ID Autogenerated").log(Level.SEVERE, "$id")

        var user = User(id, username, email, password)

        user.picture = setDefaultPicture()


        Logger.getLogger("Object User").log(Level.SEVERE, "$user")

        apiUserInterface.insertUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.code() == 400) {
                    Toast.makeText(
                        applicationContext,
                        "Username or email already in use",
                        Toast.LENGTH_LONG
                    ).show()
                    Logger.getLogger("Couldn't create the account")
                        .log(Level.SEVERE, "code=${response.code()}")
                } else if(response.code()==500){
                    Toast.makeText(
                        applicationContext,
                        "Couldn't create the account ",
                        Toast.LENGTH_LONG
                    ).show()
                    Logger.getLogger("ERROR").log(Level.SEVERE, " Account couldn´t be created code=${response.code()}")
                }else if(response.code()==201){
                    Toast.makeText(
                        applicationContext,
                        "Account successfully created ",
                        Toast.LENGTH_LONG
                    ).show()
                    Logger.getLogger("Account created").log(Level.SEVERE, "code=${response.code()}")
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()


                }

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
                Logger.getLogger("ERROR").log(Level.SEVERE, "Couldn't register the user", t)
            }
        })

    }
    //Función que se encarga de comprobar si el email introducido cumple con el formato de un email
    private fun checkEmailValid(email: String): Boolean {

        return email.matches(Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$"))
    }
    //Función que se encarga de comprobar que no haya campos vacíos
    private fun checkEmpty(
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
    }

}