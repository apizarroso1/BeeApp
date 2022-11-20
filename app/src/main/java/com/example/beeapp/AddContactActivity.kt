package com.example.beeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.ContactAdapter
import com.example.beeapp.databinding.ActivityAddContactBinding
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger

class AddContactActivity : AppCompatActivity() {

    private lateinit var svContacts:SearchView
    private lateinit var rvContacts: RecyclerView
    private lateinit var viewBinding: ActivityAddContactBinding
    lateinit var adapter: ContactAdapter
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private  var users :MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        viewBinding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.title = "Add a contact"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        initView()
    }

    fun initView(){
        rvContacts = viewBinding.rvContacts
        svContacts = viewBinding.svContacts
        initRV()
    }

    private fun initRV() {
        loadUsers()

        rvContacts.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(this,users)
        rvContacts.adapter = adapter

    }





    private fun loadUsers(){

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
                            users.removeIf {u->u.id.equals(loggedUser.id)}
                            Logger.getLogger("ListUserE").log(Level.SEVERE, "$users")
                            adapter.notifyDataSetChanged()
                        }else{
                            Logger.getLogger("BUSQUEDA").log(Level.SEVERE, "No coincide la busqueda")
                        }

                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {

                        Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to search users",t)
                    }
                })

                /*apiUserInterface.searchUser(query).enqueue(object : Callback<MutableList<User>>{
                    override fun onResponse(
                        call: Call<MutableList<User>>,
                        response: Response<MutableList<User>>
                    ) {
                        Logger.getLogger("TRYING").log(Level.SEVERE, "Amo a ver")
                        try{
                            users.clear()
                            users.addAll(response.body()!!)
                            Logger.getLogger("ListUser").log(Level.SEVERE, "$users")
                            adapter.notifyDataSetChanged()
                        }catch (e:Exception){
                            Logger.getLogger("ERROR").log(Level.SEVERE, "No se que ha pasado",e)
                        }


                    }

                    override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                        Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to search users",t)
                    }
                })*/

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
                            users.removeIf {u->u.id.equals(loggedUser.id)}
                            Logger.getLogger("ListUser").log(Level.SEVERE, "$users")
                            adapter.notifyDataSetChanged()
                        }catch (e:Exception){
                            users.clear()
                            adapter.notifyDataSetChanged()
                            Logger.getLogger("AUTO").log(Level.SEVERE, "No coincide con nada")
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
    }


}