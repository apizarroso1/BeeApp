package com.example.beeapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.activity.LoginActivity.Companion.loggedUser
import com.example.beeapp.R
import com.example.beeapp.activity.ChatActivity
import com.example.beeapp.activity.UserActivity
import com.example.beeapp.model.Chat

import com.example.beeapp.model.User
import com.example.beeapp.service.ApiChatInterface
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.create



class ContactAdapter(val context: Context, private var contacts: MutableList<User>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()
    private var apiChatInterface: ApiChatInterface = RetrofitService().getRetrofit().create()
    private lateinit var chat: Chat
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.contact_layout, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        apiChatInterface.findChat(loggedUser.id,contacts[position].id).enqueue(object :Callback<Chat>{
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                if (response.code()==200) {
                    chat = response.body()!!
                    Log.i("ADAPTER","Chat found")
                }
            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {

                Log.e("ADAPTER","Error trying to connect")
            }
        })

        holder.btnDeleteContact.setOnClickListener {

            CoroutineScope(Dispatchers.Main ).launch {
                val dialog = AlertDialog.Builder(context)
                dialog.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false).setPositiveButton("Yes"){ dialog, id->
                        loggedUser.contacts.remove(contacts[position].id)
                        contacts[position].contacts.remove(loggedUser.id)

                        apiUserInterface.updateUser(loggedUser).enqueue(object :Callback<User>{
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                Log.i("ADAPTER","Contact deleted")

                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.e("ADAPTER","ERROR trying to connect")
                            }
                        })
                        apiUserInterface.updateUser(contacts[position]).enqueue(object :Callback<User>{
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                Log.i("ADAPTER","Contact deleted(in the other user)")

                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.e("ADAPTER","ERROR trying to connect",t)
                            }
                        })
                        contacts.remove(contacts[position])
                        notifyDataSetChanged()

                        apiChatInterface.deleteChat(chat.id).enqueue(object : Callback<String>{
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                if (response.code()==200){
                                    Log.i("ADAPTER","Chat deleted ${response.body()}")
                                }else{
                                    Log.i("ADAPTER","Nope ${response.code()}, Chat=${chat.id}")
                                }
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("ADAPTER","Si es error de JSON ignorar",t)
                            }
                        })


                    }
                    .setNegativeButton("No"){dialog, id->
                        dialog.dismiss()
                    }
                val alert = dialog.create()
                alert.show()
            }


        }

        holder.btnMessage.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("username",contacts[position].username)
            intent.putExtra("uid", contacts[position].id)


            context.startActivity(intent)
        }
        holder.btnProfile.setOnClickListener {
            var intent = Intent(context, UserActivity::class.java)
            intent.putExtra("reference", contacts[position].id)

            context.startActivity(intent)
        }

        holder.tvUsername.text = contacts[position].username

    }




    fun filterList(filterlist: MutableList<User>) {
        // below line is to add our filtered
        // list in our course array list.
        contacts = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    /*fun addContact(user:User,id: String) {







    }*/

    override fun getItemCount() = contacts.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val ivProfilePicture = itemView.findViewById<ImageView>(R.id.ivProfilePicture)
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val btnDeleteContact = itemView.findViewById<Button>(R.id.btnDeleteContact)
        val btnMessage = itemView.findViewById<Button>(R.id.btnMessage)
        val btnProfile = itemView.findViewById<Button>(R.id.btnProfile)
    }

}
