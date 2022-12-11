package com.example.beeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.R
import com.example.beeapp.adapter.ContactAdapter
import com.example.beeapp.databinding.ActivityAddContactBinding
import com.example.beeapp.databinding.ActivityContactBinding
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger

class ContactActivity : AppCompatActivity() {

    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private  var users :MutableList<User> = mutableListOf()
    private lateinit var viewBinding:ActivityContactBinding
    lateinit var adapter: ContactAdapter
    private lateinit var rvContacts: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewBinding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.title = "Contacts"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



   /* private fun loadUsers(){

        svContacts.setOnQueryTextListener( object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                svContacts.clearFocus()
                // Logger.getLogger("Hola").log(Level.SEVERE, "$users")
                apiUserInterface.getUserByUsername(query).enqueue(object : Callback<User>{

                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        users.clear()

                        if(response.code()==200){
                            users.add(response.body()!!)
                            users.removeIf {u->u.id.equals(LoginActivity.loggedUser.id)}
                            Logger.getLogger("SEARCH").log(Level.INFO, "List of users found: $users")
                            adapter.notifyDataSetChanged()
                        }else{
                            Logger.getLogger("SEARCH").log(Level.INFO, "No coincidences")
                        }

                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {

                        Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to search users",t)
                    }
                })


                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                apiUserInterface.searchUser(newText).enqueue(object : Callback<MutableList<User>>{
                    override fun onResponse(
                        call: Call<MutableList<User>>,
                        response: Response<MutableList<User>>
                    ) {


                        try{
                            users.clear()
                            users.addAll(response.body()!!)
                            users.removeIf {u->u.id.equals(LoginActivity.loggedUser.id)}
                            Logger.getLogger("AUTOSEARCH").log(Level.INFO, "List of users found: $users")
                            adapter.notifyDataSetChanged()
                        }catch (e:Exception){
                            users.clear()
                            adapter.notifyDataSetChanged()
                            Logger.getLogger("AUTOSEARCH").log(Level.INFO, "No coincidences")
                        }

                        if(newText.equals("")){
                            users.clear()
                            adapter.notifyDataSetChanged()
                        }

                    }

                    override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                        Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to search users",t)
                    }
                })

                return false
            }
        })
    }*/
    //Función que se encarga de crear la barra del menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.contact_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Función que se encarga de las funciónes de cada elemento del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {


            R.id.searchContact -> {

            }

        }
        return true
    }
}