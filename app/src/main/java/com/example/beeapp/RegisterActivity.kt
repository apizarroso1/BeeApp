package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerRepeatPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var registerGoLoginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        registerEmail = findViewById(R.id.registerEmail)
        registerPassword = findViewById(R.id.registerPassword)
        registerRepeatPassword = findViewById(R.id.registerRepeatPassword)
        registerButton = findViewById(R.id.registerRegisterButton)
        registerGoLoginButton = findViewById(R.id.registerGoLoginButton)

        registerButton.setOnClickListener {
            val email = registerEmail.text.toString()
            val password = registerPassword.text.toString()
            val repeatPassword = registerRepeatPassword.text.toString()

            if (password == repeatPassword && checkEmpty(email, password, repeatPassword)) {
                register(email, password)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Passwords don't match or there are empty fields",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        registerGoLoginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Account successfully created",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Register failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkEmpty(email: String, password: String, repeatPassword: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
    }
}