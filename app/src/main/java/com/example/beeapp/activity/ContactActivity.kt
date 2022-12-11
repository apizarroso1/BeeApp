package com.example.beeapp.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.R
import com.example.beeapp.activity.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.ContactAdapter
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


    private lateinit var viewBinding:ActivityContactBinding
    lateinit var adapter: ContactAdapter
    private lateinit var rvContacts: RecyclerView
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private  var contacts :MutableList<User> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewBinding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.title = "Contacts"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        getContacts()
    }

    private fun getContacts() {
        apiUserInterface.findContacts(loggedUser.contacts).enqueue(object : Callback<List<User>>{

            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                contacts.clear()

                if(response.code()==200){
                    contacts.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                }else{
                    Logger.getLogger("SEARCH").log(Level.INFO, "Error")
                }

            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {

                Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to search users",t)
            }
        })
    }

    fun initView(){
        rvContacts = viewBinding.rvContacts
        rvContacts.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(this,contacts)
        rvContacts.adapter = adapter

    }


    //Función que se encarga de crear la barra del menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.contact_menu, menu)

        val searchItem = menu.findItem(R.id.searchContact)
        val searchView: SearchView? = searchItem?.actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                // Toast like print
                Log.i("Search","$s")

                filterContacts(s)

                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                filterContacts(s)
                return false
            }
        })


        return super.onCreateOptionsMenu(menu)
    }
    fun filterContacts(text:String){
        val filteredList: MutableList<User> = mutableListOf()
        for(item in contacts){
            if(item.username.lowercase().contains(text.lowercase())){
                filteredList.add(item)
            }
        }
       // if (filteredList.isNotEmpty()){
            adapter.filterList(filteredList)
       // }

    }
    //Función que se encarga de las funciónes de cada elemento del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home->{
                onBackPressed()
            }

            R.id.addContact->{

                startActivity(Intent(this, AddContactActivity::class.java))
            }

        }
        return true
    }
}