package com.example.beeapp.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.net.http.HttpResponseCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.R
import com.example.beeapp.model.Chat
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiChatInterface
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import retrofit2.http.HTTP
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.HashSet

class ContactAdapter(val context: Context, private val contacts: MutableList<User>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private var apiChatInterface: ApiChatInterface = RetrofitService().getRetrofit().create()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.contact_layout, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.ivProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(contacts[position].picture,0,contacts[position].picture!!.size))

        holder.btnAddContact.setOnClickListener {
            addContact(
                contacts[position],
                contacts[position].id
            )
        }

        holder.tvUsername.text = contacts[position].username

    }

    fun addContact(user:User,id: String) {

        if(!loggedUser.contacts.contains(id)){

            loggedUser.addContact(id)

            user.addContact(loggedUser.id)

            /*var participants = HashMap<String,String>()
            participants[loggedUser.id] = loggedUser.username
            participants[user.id] = user.username*/
            var participants = HashSet<String>()
            participants.add(loggedUser.id)
            participants.add(user.id)

            var chat = Chat(UUID.randomUUID().toString(),participants,HashSet())

            apiChatInterface.insertChat(chat).enqueue(object : Callback<Chat>{
                override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                    if(response.code()==201) {
                        Logger.getLogger("CHAT").log(Level.SEVERE, "Chat created ${response.code()}")
                        Toast.makeText(context, "Contact added", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Chat>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR creating chat",t)
                }
            })

            apiUserInterface.updateUser(loggedUser).enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.code()==202) {
                        Logger.getLogger("ContactAdd").log(Level.SEVERE, "Contact added in logged User? ${response.code()}")
                        Toast.makeText(context, "Contact added", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR updating logged user",t)
                }
            })

            apiUserInterface.updateUser(user).enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.code()==202) {
                        Logger.getLogger("ContactAdd").log(Level.SEVERE, "Contact added in new contact? ${response.code()}")
                        Toast.makeText(context, "Contact added", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR updating contact user",t)
                }
            })
        }else{
            Logger.getLogger("ContactAdd").log(Level.SEVERE, "Contact Couldn't be added")
            Toast.makeText(context, "Contact already added", Toast.LENGTH_LONG).show()
        }






    }

    override fun getItemCount() = contacts.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfilePicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture)
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val btnAddContact = itemView.findViewById<Button>(R.id.btnAddContact)
    }

}
