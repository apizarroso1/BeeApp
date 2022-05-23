package com.example.beeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.model.User

class UserAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.tvUsername.text = currentUser.username
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
    }
}