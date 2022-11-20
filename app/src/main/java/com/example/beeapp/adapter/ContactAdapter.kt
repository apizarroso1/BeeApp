package com.example.beeapp.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.R
import com.example.beeapp.model.User
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.math.log

class ContactAdapter(val context: Context, private val contacts: MutableList<User>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
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
                contacts[position].id
                //,contacts[position].username
            )
        }

        holder.tvUsername.text = contacts[position].username

    }

    fun addContact(id: String) {

        if(!loggedUser.contacts.contains(id)){

            loggedUser.addContact(id)

            apiUserInterface.updateUser(loggedUser).enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.code()==202) {
                        Logger.getLogger("ContactAdd").log(Level.SEVERE, "Contact added? ${response.code()}")
                        Toast.makeText(context, "Contact added", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR updating user ",t)
                }
            })
        }else{
            Logger.getLogger("ContactAdd").log(Level.SEVERE, "Contact Couldn't be added ")
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
