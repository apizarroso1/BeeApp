package com.example.beeapp.activity

//import com.bumptech.glide.Glide

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.beeapp.R
import com.example.beeapp.activity.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.ViewPagerAdapter
import com.example.beeapp.databinding.ActivityMainBinding
import com.example.beeapp.service.Const
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ua.naiksoftware.stomp.Stomp
import java.util.logging.Level
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {
    private lateinit var mainGoUserButton: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var ivProfilePicture: ImageView


    private lateinit var fragmentAdapter: ViewPagerAdapter
    private lateinit var pager: ViewPager2
    private lateinit var tabLayout: TabLayout


    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        initVar()



        pager = viewBinding.pager
        pager.adapter = fragmentAdapter
        tabLayout = viewBinding.tabLayout
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout,
            pager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Groups"
                }
                1 -> {
                    tab.text = "Chats"
                }
            }
        }
        tabLayoutMediator.attach()
        initListeners()

    }
    //Función que se encarga de inicializar las variables y demás
    private fun initVar() {
        //Se enlazan los elementos del layout con sus variables en el codigo
        mainGoUserButton = viewBinding.mainGoUserButton
        tvUsername = viewBinding.tvUsername
        ivProfilePicture = viewBinding.ivUserImage
        fragmentAdapter = ViewPagerAdapter(this)

        //Se carga el username
        tvUsername.text = loggedUser.username
        //Se carga la foto de perfil
        loadProfilePicture()
    }
    //Función que se encarga de cargar la foto de perfil
    private fun loadProfilePicture() {

        try {

            mainGoUserButton.setImageBitmap(BitmapFactory.decodeByteArray(loggedUser.picture,0,loggedUser.picture!!.size))

        } catch (e: Exception) {
            Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to load the picture",e)
        }
    }



    //Función que se encarga de inicializar los listeners
    private fun initListeners() {
        mainGoUserButton.setOnClickListener { displayUser() }
    }
    //Función que se encarga de iniciar la actividad de UserActivity
    private fun displayUser() {
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra("reference", loggedUser.id)
        startActivity(intent)
    }
    //Función que se encarga de cerrar la sesión del usuario
    private fun logout(){
        var preferences: SharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        var editor = preferences.edit()
        editor.clear()
        editor.commit()

    }
    //Función que se encarga de crear la barra del menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Función que se encarga de las funciónes de cada elemento del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            R.id.add_friend -> {
                startActivity(Intent(this, AddContactActivity::class.java))
            }

        }
        return true
    }


    override fun onResume() {
        super.onResume()
        loadProfilePicture()
    }

    override fun onDestroy() {
        super.onDestroy()
        stompClient.disconnect()
    }
}