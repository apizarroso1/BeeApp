package com.example.beeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.databinding.ActivityCreateGroupBinding
import com.example.beeapp.model.Group
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class CreateGroupActivity : AppCompatActivity() {

    private lateinit var edGroupName: EditText
    private lateinit var edGroupDescription: EditText
    private lateinit var btnCreateGroup:Button
    private lateinit var rvGroupContacts: RecyclerView
    private lateinit var viewBinding: ActivityCreateGroupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        dbRef = Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        supportActionBar?.title = "Create a group"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()

        btnCreateGroup.setOnClickListener{ checkCreation() }

    }

    private fun initView(){
        rvGroupContacts = viewBinding.rvGroupContacts
        btnCreateGroup = viewBinding.btnCreateGroup
        edGroupDescription = viewBinding.edGroupDescription
        edGroupName = viewBinding.edGroupName

    }

    private fun checkCreation(){
        val groupName = edGroupName.text.toString()
        val description = edGroupDescription.text.toString()
        if(groupName==""){
            Toast.makeText(this,groupName,Toast.LENGTH_LONG).show()
        }else{
            createGroup()
        }

    }
    private fun createGroup(){
        val groupName = edGroupName.text.toString()
        val description = edGroupDescription.text.toString()
        val userList = ArrayList<String>()

        userList.add(auth.currentUser?.uid.toString())

        val group  = dbRef.child("groups").push().key

        dbRef.child("groups").child(group.toString()).setValue(Group(group.toString(),groupName,description,userList))
        finish()

    }


}