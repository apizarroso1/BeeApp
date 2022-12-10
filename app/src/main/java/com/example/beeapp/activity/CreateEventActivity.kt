package com.example.beeapp.activity


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.get

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.activity.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.AddToGroupAdapter
import com.example.beeapp.databinding.ActivityCreateEventBinding
import com.example.beeapp.model.*
import com.example.beeapp.service.ApiEventInterface
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService

import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.ArrayList


class CreateEventActivity : AppCompatActivity() {

    private lateinit var edEventName: EditText
    private lateinit var edEventDescription: EditText
    private lateinit var btnCreateGroup: Button
    private lateinit var rvGroupContacts: RecyclerView
    private lateinit var chipGroup: ChipGroup
    private lateinit var edDate: EditText
    private lateinit var edTime: EditText

    private lateinit var viewBinding: ActivityCreateEventBinding

    private lateinit var group: String

    private var type:EventType = EventType.LEISURE

    private lateinit var adapter: AddToGroupAdapter
    //lista de id+nombre de los contactos agregados al grupo
    private lateinit var attendees: MutableSet<String>
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



        attendees = mutableSetOf()

        attendees.add(loggedUser.id)

        contactList = ArrayList()
        adapter = AddToGroupAdapter(this, contactList, attendees)
        initView()
        rvGroupContacts.layoutManager = LinearLayoutManager(this)
        rvGroupContacts.adapter = adapter
        getContacts()

        btnCreateGroup.setOnClickListener { checkCreation() }

        edDate.setOnClickListener{
            showDatePickerDialog()
        }
        edTime.setOnClickListener { showTimePickerDialog() }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->


            if(checkedIds.isNotEmpty()) {
                when (checkedIds[0]) {

                    group[0].id -> type = type.setType(viewBinding.cLEISURE.text.toString())

                    group[1].id -> type = type.setType(viewBinding.cBUSINESS.text.toString())

                    group[2].id -> type = type.setType(viewBinding.cFAMILY.text.toString())

                    group[3].id -> type = type.setType(viewBinding.cSPORTS.text.toString())


                }
            }

        }

    }

    private fun setDefaultTime(){
        val time = "00:00"
        edTime.setText(time)
    }

    private fun setTodayDate(){
        var cal:Calendar = Calendar.getInstance()

        var year = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)
        val selectedDate = "$day/${month+1}/$year"
        edDate.setText(selectedDate)

    }

    private fun showTimePickerDialog(){
        val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->

            var time = String.format(Locale.getDefault(),"%02d:%02d",hour,minute)

            edTime.setText(time)

        }

        var time = edTime.text.split(":")

        var hour = time[0].trim().toInt()
        var minute = time[1].trim().toInt()

        var timePickerDialog = TimePickerDialog(this,timeSetListener,hour,minute,true)

        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()

    }


    private fun showDatePickerDialog() {


        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->

            val selectedDate = "$day/${month+1}/$year"
            edDate.setText(selectedDate)
        }

        var date = edDate.text.split("/")

        var year = date[2].trim().toInt()
        var month = date[1].trim().toInt()-1
        var day = date[0].trim().toInt()

        var datePickerDialog = DatePickerDialog(this,dateSetListener,year,month,day)

        datePickerDialog.show()
    }

    //enlazamos los elementos del layout
    private fun initView() {
        rvGroupContacts = viewBinding.rvGroupContacts
        btnCreateGroup = viewBinding.btnCreateGroup
        edEventDescription = viewBinding.edGroupDescription
        edEventName = viewBinding.edEventName
        chipGroup = viewBinding.chipGroup
        edDate = viewBinding.edDate
        edTime = viewBinding.edTime
        setTodayDate()
        setDefaultTime()

    }

    //funcion para comprobar si el grupo tiene al menos un nombre para poder ser creado
    private fun checkCreation() {
        val groupName = edEventName.text.toString()
        if (groupName == "") {
            Toast.makeText(this, "No name introduced", Toast.LENGTH_LONG).show()
        } else {
            createEvent()
        }

    }

    //funcion para crear en la base de datos el grupo
    private fun createEvent() {
        val eventName = edEventName.text.toString()
        val description = edEventDescription.text.toString()
        val eventDate = edDate.text.toString()
        val eventTime = edTime.text.toString()

        var chat = Chat(UUID.randomUUID().toString(),attendees,ArrayList(),ChatType.EVENT)

        var event = Event(eventName,description,attendees,eventDate,eventTime,type,chat.id)

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


    }


}