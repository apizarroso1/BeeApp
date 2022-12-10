package com.example.beeapp.activity


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap

import com.example.beeapp.activity.LoginActivity.Companion.loggedUser

import com.example.beeapp.databinding.ActivityUserBinding
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.logging.Level
import java.util.logging.Logger


class UserActivity : AppCompatActivity() {
    private lateinit var ivProfilePicture: ImageView
    private lateinit var viewBinding: ActivityUserBinding
    private lateinit var tvUsername:TextView
    private lateinit var tvMood:TextView

    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private lateinit var dialog: AlertDialog
    private lateinit var editMood: EditText
    private lateinit var selectedImage: Uri
    private lateinit var reference:String
    private lateinit var contact:User

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            selectedImage = data?.data!!
            uploadImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.userView.visibility = View.INVISIBLE
        initView()
        //initData()
        CoroutineScope(Dispatchers.Main).launch {
            load()
        }

    }



    //Función que se encarga de inicializar los datos y demás cosas necesarias
    private fun initData(){

        reference = intent.getStringExtra("reference").toString()

        if(reference== loggedUser.id) {
            loadUserInfo()
            initListeners()
        }else{
            loadContactInfo()

        }


    }

    private  fun load(){

            initData()
    }



    //Función que se encarga de configurar el editText del cuadro de diálogo
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
                        Logger.getLogger("CHANGED").log(Level.INFO, "Mood changed. code:${response.code()}")
                    }else {
                        Toast.makeText(
                            applicationContext,
                            "Couldn't change the mood",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("NOT CHANGED").log(Level.SEVERE, "Couldn't change the mood. code=${response.code()}")


                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to change the mood",t)
                }
            })

        }
    }

    //Función que se encarga de inicializar los elementos del layout
    private fun initView(){

        ivProfilePicture = viewBinding.ivProfilePicture
        tvUsername = viewBinding.tvUsername
        tvMood = viewBinding.tvMood

        //Glide.with(this@UserActivity).load(setProfilePicture()).into(ivProfilePicture)




    }
    //Función que se encarga de convertir el ByteArray en un Bitmap para cargar la imagen
    private fun setProfilePicture(user:User){
        ivProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(user.picture,0,user.picture!!.size))

    }
    //Función que se encarga de inicializar los listeners
    private fun initListeners(){
        ivProfilePicture.setOnClickListener{editProfilePicture()}
        tvMood.setOnClickListener {

            editMood.setText(tvMood.text)
            dialog.show()
        }

    }


    //Función que se encarga de cambiar la foto de perfil
    private fun editProfilePicture(){
        openActivityForResult()
    }

    //Función que se encarga de abrir la galeria para elegir una imagen
    private fun openActivityForResult() {
        startForResult.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

    private fun loadUserInfo() {

        tvMood.text = loggedUser.mood
        tvUsername.text = loggedUser.username
        dialog = AlertDialog.Builder(this).create()
        dialog.setTitle("Mood")
        editMood = EditText(this)
        editMood.filters += InputFilter.LengthFilter(120)
        setEditMoodButton()
        dialog.setView(editMood)

        setProfilePicture(loggedUser)
        viewBinding.userView.visibility = View.VISIBLE
        viewBinding.progressBar.visibility = View.GONE
    }

    private fun loadContactInfo(){

        apiUserInterface.getUserById(reference).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {

                try {
                    contact = User(response.body()!!)

                    tvMood.text = contact.mood
                    tvUsername.text = contact.username
                    setProfilePicture(contact)
                    viewBinding.userView.visibility = View.VISIBLE
                    viewBinding.progressBar.visibility = View.GONE

                }catch (e:Exception){
                    Log.e("CONTACTINFO","ERROR parsing contact")
                }

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("CONTACTINFO","ERROR trying to connect to load contact info")
            }
        })
    }

    //Función que se encarga de cargar la imagen a la base de datos y cambiarla en el layout
    private fun uploadImage(){
        var imageStream:InputStream
        var originBitmap:Bitmap? = null
        try {
            imageStream = contentResolver.openInputStream(selectedImage)!!
            originBitmap = BitmapFactory.decodeStream(imageStream)
        }catch (e:Exception){
            Logger.getLogger("ERROR").log(Level.SEVERE, "Error with the image",e)
        }
        if(originBitmap!=null){
            ivProfilePicture.setImageURI(selectedImage)
            var bitmap =  ivProfilePicture.drawable.toBitmap()
            var byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream)
            var bytes = byteArrayOutputStream.toByteArray()

            loggedUser.picture = bytes

            apiUserInterface.updateUser(loggedUser).enqueue(object :Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if(response.code()==202){
                        Toast.makeText(
                            applicationContext,
                            "Picture changed",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("CHANGED").log(Level.INFO, "Picture changed. code:${response.code()}")
                    }else {
                    Toast.makeText(
                        applicationContext,
                        "Couldn't change the picture",
                        Toast.LENGTH_LONG
                    ).show()
                    Logger.getLogger("NOT CHANGED").log(Level.SEVERE, "Couldn't change the picture. code=${response.code()}")


                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to change the picture",t)
                }
            })

        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home ->{
                onBackPressed()
                finish()
                return true
            }

        }
        return true
    }

}