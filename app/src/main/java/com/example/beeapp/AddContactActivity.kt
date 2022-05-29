package com.example.beeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.databinding.ActivityAddContactBinding
import com.example.beeapp.databinding.ActivityMainBinding
import com.example.beeapp.databinding.ActivityUserBinding
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddContactActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var viewBinding: ActivityAddContactBinding
    lateinit var adapter: ContactAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private var users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        viewBinding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference

        supportActionBar?.title = "Add a contact"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()
    }

    fun initView(){
        rvUsers = viewBinding.rvContacts
        initRV()
    }

    private fun initRV() {
        loadUsers()
        rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(this,users)
        rvUsers.adapter = adapter
    }

    private fun loadUsers(){
        dbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (auth.currentUser?.uid!= currentUser?.uid){
                        users.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "Something went wrong")
            }

        })
    }
}