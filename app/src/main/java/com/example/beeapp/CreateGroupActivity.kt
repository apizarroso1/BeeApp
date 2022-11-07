package com.example.beeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.adapter.AddToGroupAdapter
import com.example.beeapp.databinding.ActivityCreateGroupBinding
import com.example.beeapp.model.Group
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class CreateGroupActivity : AppCompatActivity() {

    private lateinit var edGroupName: EditText
    private lateinit var edGroupDescription: EditText
    private lateinit var btnCreateGroup: Button
    private lateinit var rvGroupContacts: RecyclerView
    private lateinit var viewBinding: ActivityCreateGroupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var group: String
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: AddToGroupAdapter
    //lista de id+nombre de los contactos agregados al grupo
    private lateinit var addToGroupList: MutableMap<String, String>
    //lista de contactos del usuario que esta creando el grupo
    private lateinit var contactList: ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportActionBar?.title = "Create a group"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dbRef =
            Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        //creamos un id nuevo para el grupo que queremos crear
        group = dbRef.child("groups").push().key.toString()


        addToGroupList = HashMap()
        contactList = ArrayList()
        adapter = AddToGroupAdapter(this, contactList, addToGroupList)
        initView()
        rvGroupContacts.layoutManager = LinearLayoutManager(this)
        rvGroupContacts.adapter = adapter
        getContacts()

        btnCreateGroup.setOnClickListener { checkCreation() }

    }

    //enlazamos los elementos del layout
    private fun initView() {
        rvGroupContacts = viewBinding.rvGroupContacts
        btnCreateGroup = viewBinding.btnCreateGroup
        edGroupDescription = viewBinding.edGroupDescription
        edGroupName = viewBinding.edGroupName
    }

    //funcion para comprobar si el grupo tiene al menos un nombre para poder ser creado
    private fun checkCreation() {
        val groupName = edGroupName.text.toString()
        if (groupName == "") {
            Toast.makeText(this, groupName, Toast.LENGTH_LONG).show()
        } else {
            createGroup()
        }

    }

    //funcion para crear en la base de datos el grupo
    private fun createGroup() {
        val groupName = edGroupName.text.toString()
        val description = edGroupDescription.text.toString()


        dbRef.child("groups").child(group)
            .setValue(Group(group, groupName, description, addToGroupList))
        finish()
    }

    //funcion para rellenar la lista de contactos del usuario que esta creando el grupo
    private fun getContacts() {

        var contactsUidList: MutableList<String> = ArrayList()

        //primero busca en la base de datos los id de los contactos agregados
        dbRef.child("users").child(auth.currentUser?.uid.toString()).child("contacts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    contactsUidList.clear()
                    for (postSnapshot in snapshot.children) {
                        val currentUid = postSnapshot.key
                        contactsUidList.add(currentUid!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Database", "cancelled request")
                }

            })

        //despues busca entre los usuarios de la base de datos los que tienen el id
        // y los mete en la lista de objetos de la clase User para luego ser mostrados
        // en items en el recycler view
       /* dbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(auth.currentUser?.uid.equals(currentUser?.uid)){
                        addToGroupList[auth.currentUser?.uid!!] = currentUser?.username.toString()
                    }
                    else if (auth.currentUser?.uid != currentUser?.uid && contactsUidList.contains(
                            currentUser?.uid.toString()
                        )
                    ) {
                        contactList.add(currentUser!!)

                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Database", "cancelled request")
            }

        })*/
    }


}