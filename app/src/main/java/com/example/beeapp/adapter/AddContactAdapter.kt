package com.example.beeapp.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.R
import com.example.beeapp.activity.LoginActivity
import com.example.beeapp.model.Chat
import com.example.beeapp.model.ChatType
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiChatInterface
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class AddContactAdapter(val context: Context, private val contacts: MutableList<User>) :
    RecyclerView.Adapter<AddContactAdapter.AddContactViewHolder>() {

    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private var apiChatInterface: ApiChatInterface = RetrofitService().getRetrofit().create()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddContactViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.add_contact_layout, parent, false)

        return AddContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddContactViewHolder, position: Int) {
        holder.ivProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(contacts[position].picture,0,contacts[position].picture!!.size))

        holder.btnAddContact.setOnClickListener {
            addContact(
                contacts[position],
                contacts[position].id
            )
        }

        holder.tvUsername.text = contacts[position].username

    }

    fun addContact(user: User, id: String) {

        if(!LoginActivity.loggedUser.contacts.contains(id)){

            LoginActivity.loggedUser.addContact(id)

            user.addContact(LoginActivity.loggedUser.id)

            /*var participants = HashMap<String,String>()
            participants[loggedUser.id] = loggedUser.username
            participants[user.id] = user.username*/
            var participants = HashSet<String>()
            participants.add(LoginActivity.loggedUser.id)
            participants.add(user.id)

            var chat = Chat(UUID.randomUUID().toString(),participants, ArrayList(), ChatType.PRIVATE)

            apiChatInterface.insertChat(chat).enqueue(object : Callback<Chat> {
                override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                    if(response.code()==201) {

                        Logger.getLogger("CHAT").log(Level.INFO,"Chat created ${response.code()}")
                        Toast.makeText(context, "Contact added", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Chat>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR creating chat",t)
                }
            })

            apiUserInterface.updateUser(LoginActivity.loggedUser).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.code()==202) {
                        Logger.getLogger("ContactAdd").log(Level.INFO, "Contact added in logged User? ${response.code()}")
                        Toast.makeText(context, "Contact added", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR updating logged user",t)
                }
            })

            apiUserInterface.updateUser(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.code()==202) {
                        Logger.getLogger("ContactAdd").log(Level.INFO, "Contact added in new contact? ${response.code()}")
                        Toast.makeText(context, "Contact added", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR updating contact user",t)
                }
            })
        }else{
            Logger.getLogger("ContactAdd").log(Level.INFO, "Contact Couldn't be added")
            Toast.makeText(context, "Contact already added", Toast.LENGTH_LONG).show()
        }






    }

    override fun getItemCount() = contacts.size

    class AddContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfilePicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture)
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val btnAddContact = itemView.findViewById<Button>(R.id.btnAddContact)
    }

}