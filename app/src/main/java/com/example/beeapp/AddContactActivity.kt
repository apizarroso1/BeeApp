package com.example.beeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.databinding.ActivityAddContactBinding
import com.example.beeapp.databinding.ActivityMainBinding
import com.example.beeapp.databinding.ActivityUserBinding
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseUser

class AddContactActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var viewBinding: ActivityAddContactBinding
    lateinit var adapter: ContactAdapter

    private var users = ArrayList<User>()

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
        rvUsers = viewBinding.rvContacts
        initListeners()
        initRV()
    }

    private fun initRV() {
        // Storage -> arraylist
        rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(users) { addContact(it) }
        rvUsers.adapter = adapter
    }

    private fun addContact(uId: String) {
        //users.remove() mediante id
        adapter.notifyDataSetChanged()
        //TODO("Add user to contact list + create chat")
    }

    private fun initListeners() {
        //TODO("Not yet implemented")
    }
}