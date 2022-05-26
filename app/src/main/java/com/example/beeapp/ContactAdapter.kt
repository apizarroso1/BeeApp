package com.example.beeapp

import android.content.Context
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beeapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContactAdapter(val context: Context, private val contacts:ArrayList<User>, private val onAddContact: (String) ->Unit):RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactAdapter.ContactViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.contact_layout, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.render(contacts[position].profilePicture!!, contacts[position].username!!, contacts[position].uid!!, onAddContact)

        var imageRef: String? = null
        var dbRef = Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference

        dbRef.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (postSnapshot in snapshot.children){
                        val currentUser = postSnapshot.getValue(User::class.java)

                        if (contacts[holder.adapterPosition].uid.equals(currentUser?.uid) ){
                            imageRef = currentUser?.profilePicture
                        }
                        Glide.with(context).load(imageRef).into(holder.ivProfilePicture)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    //NADA
                }
            })

    }

    override fun getItemCount() = contacts.size

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         val ivProfilePicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture)
        private val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        private val btnAddContact = itemView.findViewById<Button>(R.id.btnAddContact)

        fun render(profilePictureUri: String, username: String, uId:String,  onAddContact: (String) -> Unit){
            tvUsername.text = username
            btnAddContact.setOnClickListener { onAddContact(uId) }
            //ivProfilePicture.setImageURI(profilePictureUri)
        }
    }

}
