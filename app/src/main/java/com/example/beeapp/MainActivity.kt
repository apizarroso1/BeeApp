package com.example.beeapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.viewpager2.widget.ViewPager2
//import com.bumptech.glide.Glide
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.ViewPagerAdapter
import com.example.beeapp.databinding.ActivityMainBinding
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import com.example.beeapp.service.SocketListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
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
            //var imageBytes = loggedUser.picture

            //var profilePicture: Bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes!!.size)
            //Glide.with(this@MainActivity).load(profilePicture).into(mainGoUserButton)
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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

            /*R.id.search_action -> {
                val actionView = item.actionView as SearchView
                actionView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        Log.e("ERROR", "Something went wrong")
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        tempList.clear()
                        val searchText = newText!!.lowercase(Locale.getDefault())

                        if (searchText.isNotEmpty()) {
                            userList.forEach {
                                if (it.username?.lowercase(Locale.getDefault())!!.contains(searchText)) {
                                    tempList.add(it)
                                }
                            }

                            rvChats.adapter?.notifyDataSetChanged()
                        } else {
                            tempList.clear()
                            tempList.addAll(userList)
                            rvChats.adapter!!.notifyDataSetChanged()
                        }

                        ("No modifica el rv por algún motivo")
                        return false
                    }
                })
            }*/
        }
        return true
    }
}