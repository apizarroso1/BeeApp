package com.example.beeapp

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.model.User

class ContactAdapter(private val contacts:ArrayList<User>, private val onAddContact: (String) ->Unit):RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactAdapter.ContactViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.contact_layout, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.render(contacts[position].profilePicture!!, contacts[position].username!!, contacts[position].uid!!, onAddContact)
    }

    override fun getItemCount() = contacts.size

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivProfilePicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture)
        private val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        private val btnAddContact = itemView.findViewById<Button>(R.id.btnAddContact)

        fun render(profilePictureUri: String, username: String, uId:String,  onAddContact: (String) -> Unit){
            tvUsername.text = username
            btnAddContact.setOnClickListener { onAddContact(uId) }
            //TODO("Vincular url de imagen con imageview")
            //ivProfilePicture.setImageURI(profilePictureUri)
        }
    }

}

