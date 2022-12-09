package com.example.beeapp

import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.AddToGroupAdapter
import com.example.beeapp.databinding.ActivityCreateEventBinding
import com.example.beeapp.model.Event
import com.example.beeapp.model.EventType
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiEventInterface
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService

import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.UUID
import java.util.logging.Level
import java.util.logging.Logger



class CreateEventActivity : AppCompatActivity() {

    private lateinit var edEventName: EditText
    private lateinit var edEventDescription: EditText
    private lateinit var btnCreateGroup: Button
    private lateinit var rvGroupContacts: RecyclerView
    private lateinit var chipGroup: ChipGroup

    private lateinit var viewBinding: ActivityCreateEventBinding

    private lateinit var group: String

    private var type:EventType = EventType.LEISURE

    private lateinit var adapter: AddToGroupAdapter
    //lista de id+nombre de los contactos agregados al grupo
    private lateinit var addToEventList: MutableSet<String>
    //lista de contactos del usuario que esta creando el grupo
    private lateinit var contactList: ArrayList<User>

    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private var apiEventInterface: ApiEventInterface = RetrofitService().getRetrofit().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportActionBar?.title = "Create a group"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        addToEventList = mutableSetOf()

        addToEventList.add(loggedUser.id)

        contactList = ArrayList()
        adapter = AddToGroupAdapter(this, contactList, addToEventList)
        initView()
        rvGroupContacts.layoutManager = LinearLayoutManager(this)
        rvGroupContacts.adapter = adapter
        getContacts()

        btnCreateGroup.setOnClickListener { checkCreation() }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->


            if(checkedIds.isNotEmpty()) {
                when (checkedIds[0]) {

                    group[0].id -> type = type.setType(viewBinding.cLEISURE.text.toString())

                    group[1].id -> type = type.setType(viewBinding.cBUSINESS.text.toString())

                    group[2].id -> type = type.setType(viewBinding.cFAMILY.text.toString())

                    group[3].id -> type = type.setType(viewBinding.cSPORTS.text.toString())


                }
            }
                Log.d("Checked id","$type")
                Log.d("group id","${group.checkedChipId}")
                //Log.d("chip id","${chip.id}")

        }

    }

    //enlazamos los elementos del layout
    private fun initView() {
        rvGroupContacts = viewBinding.rvGroupContacts
        btnCreateGroup = viewBinding.btnCreateGroup
        edEventDescription = viewBinding.edGroupDescription
        edEventName = viewBinding.edEventName
        chipGroup = viewBinding.chipGroup
    }

    //funcion para comprobar si el grupo tiene al menos un nombre para poder ser creado
    private fun checkCreation() {
        val groupName = edEventName.text.toString()
        if (groupName == "") {
            Toast.makeText(this, groupName, Toast.LENGTH_LONG).show()
        } else {
            createEvent()
        }

    }

    //funcion para crear en la base de datos el grupo
    private fun createEvent() {
        val eventName = edEventName.text.toString()
        val description = edEventDescription.text.toString()
        val eventId = UUID.randomUUID().toString()

        var event = Event(eventId,eventName,description,addToEventList,null,null,null,type)

        apiEventInterface.insertEvent(event).enqueue(object :Callback<Event>{
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                Logger.getLogger("Created").log(Level.INFO, "Event=${response.body()}, code=${response.code()}")
                finish()
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Logger.getLogger("Event").log(Level.SEVERE, "Error connecting",t)
            }
        })


    }

    //funcion para rellenar la lista de contactos del usuario que esta creando el grupo
    private fun getContacts() {


        apiUserInterface.findContacts(loggedUser.contacts).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                Logger.getLogger("ContactList").log(Level.INFO, "Contacts=${response.body()}, code=${response.code()}")
                if(!response.body().isNullOrEmpty()){
                    contactList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()


                }

            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Logger.getLogger("ContactList").log(Level.SEVERE, "Error connecting",t)
            }
        })


        //primero busca en la base de datos los id de los contactos agregados
       /* dbRef.child("users").child(auth.currentUser?.uid.toString()).child("contacts")
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

            })*/

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