package com.example.beeapp


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.beeapp.LoginActivity.Companion.loggedUser

import com.example.beeapp.databinding.ActivityUserBinding
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.math.log

class UserActivity : AppCompatActivity() {
    private lateinit var ivProfilePicture: ImageView
    private lateinit var viewBinding: ActivityUserBinding
    private lateinit var tvUsername:TextView
    private lateinit var tvMood:TextView

    private lateinit var username:String
    private var mood:String = ""
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private lateinit var dialog: AlertDialog
    private lateinit var editMood: EditText
    private lateinit var selectedImage: Uri

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
        username=loggedUser.username

        initView()
        initData()
        initListeners()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }


    //Función que se encarga de inicializar los datos y demás cosas necesarias
    private fun initData(){

        mood = loggedUser.mood
        tvMood.text = "$mood"
        dialog = AlertDialog.Builder(this).create()
        dialog.setTitle("Mood")
        editMood = EditText(this)
        setEditMoodButton()
        dialog.setView(editMood)


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
                        Logger.getLogger("Mood changed").log(Level.SEVERE, "code:${response.code()}")
                    }else {
                        Toast.makeText(
                            applicationContext,
                            "Couldn't change the mood",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("Couldn't change the mood").log(Level.SEVERE, "code=${response.code()}")


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
        tvUsername.text = "$username"
        tvMood.text = "$mood"

        Glide.with(this@UserActivity).load(setProfilePicture()).into(ivProfilePicture)

    }
    //Función que se encarga de convertir el ByteArray en un Bitmap para cargar la imagen
    private fun setProfilePicture():Bitmap{

        var imageBytes = loggedUser.picture

        return BitmapFactory.decodeByteArray(imageBytes,0,imageBytes!!.size)
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
            //var imageString = Base64.encodeToString(bytes,Base64.DEFAULT)

            loggedUser.picture = bytes

            apiUserInterface.updateUser(loggedUser).enqueue(object :Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if(response.code()==202){
                        Toast.makeText(
                            applicationContext,
                            "Picture changed",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("Picture changed").log(Level.SEVERE, "code:${response.code()}")
                    }else {
                    Toast.makeText(
                        applicationContext,
                        "Couldn't change the picture",
                        Toast.LENGTH_LONG
                    ).show()
                    Logger.getLogger("Couldn't change the picture").log(Level.SEVERE, "code=${response.code()}")


                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to change the picture",t)
                }
            })

        }
    }


}