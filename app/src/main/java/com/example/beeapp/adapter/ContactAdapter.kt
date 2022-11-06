package com.example.beeapp.adapter

import android.content.Context
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
import com.example.beeapp.R
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContactAdapter(val context: Context, private val contacts: ArrayList<User>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.contact_layout, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        var imageRef: String? = null
        dbRef =
            Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()

        holder.btnAddContact.setOnClickListener {
            addContact(
                contacts[position].uid.toString(),
                contacts[position].username.toString()
            )
        }

        holder.tvUsername.text = contacts[position].username

        dbRef.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (postSnapshot in snapshot.children) {
                        val currentUser = postSnapshot.getValue(User::class.java)

                        try {
                            if (contacts[holder.adapterPosition].uid.equals(currentUser?.uid)) {
                               // imageRef = currentUser?.profilePicture
                            }
                            Glide.with(context).load(imageRef).into(holder.ivProfilePicture)
                        } catch (e: Exception) {
                            e.stackTrace
                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("ERROR", "Something went wrong")
                }
            })



    }

    fun addContact(uId: String, username: String) {
        Toast.makeText(context, "Contact Added", Toast.LENGTH_LONG).show()
        dbRef.child("users").child(auth.currentUser?.uid.toString()).child("contacts").child(uId)
            .setValue(username)
    }

    override fun getItemCount() = contacts.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfilePicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture)
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val btnAddContact = itemView.findViewById<Button>(R.id.btnAddContact)
    }

}
