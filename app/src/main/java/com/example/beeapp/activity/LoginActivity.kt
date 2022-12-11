package com.example.beeapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.beeapp.R
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
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()

    companion object {
        //objeto de la clase User que se usará durante el resto de la ejecución de la app para no tener que hacer continuamente llamadas a la api
        lateinit var loggedUser: User
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        //Se comprueba que haya una sesión iniciada anteriormente
        checkLoggedUser()

        loginEmail = findViewById(R.id.loginEmail)
        loginPassword = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginLoginButton)
        loginGoRegisterButton = findViewById(R.id.loginGoRegisterButton)

        loginButton.setOnClickListener {

            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            //Se comprueba que al pulsar el boton de login haya o no campos vacíos
            if (checkEmpty(email, password)) {

                //En caso de que no los haya se intenta iniciar sesión
                login(email, password)
            } else {

                //Se muestra por pantalla al usuario que hay campos vacíos
                Toast.makeText(applicationContext, "There are empty fields", Toast.LENGTH_LONG)
                    .show()
            }
        }

        loginGoRegisterButton.setOnClickListener {

            //Al pulsar el boton de registrarse se inicia la actividad para registrar a un usuario
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()

        }

    }
    //Función que se encarga de ver si hay una sesión iniciada
    private fun checkLoggedUser(){

        var preferences: SharedPreferences = getSharedPreferences("credentials",Context.MODE_PRIVATE)
        var id = preferences.getString("userid","No info")

        Logger.getLogger("SharedPreferences").log(Level.INFO, "$id")

        //Se comprueba si hay info en las sharedPreferences
        if(!id.equals("No info")){

            //En caso afirmativo se produce una llamada a la api para buscar un usuario usando el username
            apiUserInterface.getUserById(id).enqueue(object: Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if (response.code()==200){
                        loggedUser = response.body()!!

                        //Log para saber que se ha encontrado una sesión iniciada
                        Logger.getLogger("Session found").log(Level.INFO, "${response.body()}")

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }


                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    //Log para saber que error se ha producido
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to get User",t)
                }
            })


        }else{
            Logger.getLogger("Sesion not found").log(Level.INFO, "Try to login")
        }

    }


    //Función que se encarga de guardar la sesión
    private fun saveSession(){
        var preferences: SharedPreferences = getSharedPreferences("credentials",Context.MODE_PRIVATE)
        var id = loggedUser.id

        var editor = preferences.edit()

        //Se guarda en las sharedPreferences el username del usuario loggeado
        editor.putString("userid",id)

        editor.commit()

    }
    //Funcion login hace una consulta con el email indicado
    private fun login(email: String, password: String) {
        val hashedPassword = RegisterActivity.toMd5Hash(password)

        //Llamada a la api para buscar un usuario usando el email
        apiUserInterface.getUserByEmail(email).enqueue(object: Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {


                //Se comprueba primero que la respuesta sea que se ha encontrado al usuario
                if (response.code()==200){

                    //Se comprueba que la contraseña coincida con la contraseña hasheada en el registro
                    if(response.body()!!.password.equals(hashedPassword)){
                        //Se muestra por pantalla al usuario que ha iniciado sesión
                        Toast.makeText(
                            applicationContext,
                            "Logged ",
                            Toast.LENGTH_LONG
                        ).show()

                        //Se cargan los datos al objeto de la clase User desde la base de datos
                        loggedUser = response.body()!!
                        //Log para saber que la respuesta ha sido la esperada
                        Logger.getLogger("USER LOGGED").log(Level.SEVERE, "${response.body()}")

                        //Se guarda la sesión
                        saveSession()
                        //Se pasa a la pantalla de la Main Activity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        //Se muestra en pantalla que la contraseña no es correcta
                        Toast.makeText(
                            applicationContext,
                            "Incorrect password ",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }else{
                    //Se muestra por pantalla al usuario que el email introducido no corresponde con ningún usuario registrado
                    Toast.makeText(
                        applicationContext,
                        "User not found",
                        Toast.LENGTH_LONG
                    ).show()
                    //Log para saber que la respuesta es la esperada al no haber encontrado al usuario
                    Logger.getLogger("User not found").log(Level.INFO, "code=${response.code()}")


                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                //En caso de que el error sea del server se avisa que algo ha ido mal
                Toast.makeText(
                    applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
                //Log para saber que error se ha producido
                Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to login",t)
            }


        })

    }
    //Función para comprobar que los campos no están vacíos
    private fun checkEmpty(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }




}