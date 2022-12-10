package com.example.beeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.beeapp.R
import com.example.beeapp.model.User
import com.google.android.material.chip.Chip

//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase

class AddToGroupAdapter(val context: Context, val userList: ArrayList<User>, val addToGroupList: MutableSet<String>):
RecyclerView.Adapter<AddToGroupAdapter.AddToGroupViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddToGroupViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.add_to_event_layout, parent, false)

        return AddToGroupViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AddToGroupViewHolder,
        position: Int
    ) {
        val currentUser = userList[position]
        holder.tvUsername.text = currentUser.username
        Glide.with(context).load(currentUser.picture).into(holder.ivProfilePicture)


        holder.btnAddToGroup.setOnClickListener{

            if(!addToGroupList.contains(currentUser.id)) {
                addToGroupList.add(currentUser.id)
                holder.btnAddToGroup.text = "Added"
            }else{
                addToGroupList.remove(currentUser.id)
                holder.btnAddToGroup.text = "Add"
            }

        }

    }

    override fun getItemCount() = userList.size

    class AddToGroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val ivProfilePicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture)
        val btnAddToGroup = itemView.findViewById<Chip>(R.id.btnAddToGroup)
    }
}

