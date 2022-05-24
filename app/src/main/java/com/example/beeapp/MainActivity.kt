package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.databinding.ActivityMainBinding
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var goUserButton: ImageView
    private lateinit var rvChats:RecyclerView
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var tempList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initVar()
        initListeners()
        rvChats()
    }

    private fun initVar(){
        auth = FirebaseAuth.getInstance()
        dbRef = Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        goUserButton = viewBinding.mainGoUserButton
        rvChats = viewBinding.rvChats

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        rvChats.layoutManager = LinearLayoutManager(this)
        rvChats.adapter = adapter

    }
    private fun rvChats(){
        dbRef.child("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (auth.currentUser?.uid!= currentUser?.uid){
                        userList.add(currentUser!!)

                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initListeners(){
        goUserButton.setOnClickListener { displayUser() }
    }

    private fun displayUser(){
        //startActivity(Intent(this, UserActivity::class.java))
        //finish()
        Toast.makeText(this, "SI va", Toast.LENGTH_LONG).show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.logout ->{
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            R.id.search_action -> {
                val actionView = item.actionView as SearchView
                actionView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        TODO("Not yet implemented")
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

                        TODO("No modifica el rv por algún motivo")
                        return false
                    }
                })
            }
        }
        return true
    }
}