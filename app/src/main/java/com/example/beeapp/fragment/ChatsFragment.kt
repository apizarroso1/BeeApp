package com.example.beeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.activity.LoginActivity.Companion.loggedUser
import com.example.beeapp.R
import com.example.beeapp.adapter.UserAdapter
import com.example.beeapp.model.Chat
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiChatInterface
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger


class ChatsFragment : Fragment() {
    private lateinit var contactList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var rvChats: RecyclerView
    private var apiChatInterface: ApiChatInterface = RetrofitService().getRetrofit().create()
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    var chatsList: MutableList<Chat> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View = inflater.inflate(R.layout.fragment_chats, container, false)
        rvChats = view.findViewById(R.id.rvChats)


        contactList = arrayListOf()
        adapter = UserAdapter(requireContext(), contactList)

        rvChats.layoutManager = LinearLayoutManager(requireContext())
        rvChats.adapter = adapter
        loadContacts()
        //rvChats()
        return view

    }
    //se añaden los usuarios a la lista de usuarios
    private fun loadContacts() {

        apiUserInterface.findContacts(loggedUser.contacts).enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                Logger.getLogger("ContactList").log(Level.INFO, "Contacts=${response.body()}, code=${response.code()}")
                if(!response.body().isNullOrEmpty()){
                    contactList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()


                }

            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Logger.getLogger("ContactList").log(Level.SEVERE,  "ERROR trying to connect")
            }
        })

    }

/*
    private fun rvChats() {



        apiChatInterface.findAllChatsFromUser(loggedUser.id).enqueue(object : Callback<List<Chat>>{
            override fun onResponse(call: Call<List<Chat>>, response: Response<List<Chat>>) {

                chatsList.addAll(response.body()!!)

                Logger.getLogger("ListChats").log(Level.SEVERE, "$chatsList code=${response.code()}")

            }

            override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })




    }*/


}