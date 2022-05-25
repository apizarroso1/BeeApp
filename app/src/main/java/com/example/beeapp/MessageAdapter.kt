package com.example.beeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>,val username: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVED = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieved_layout, parent, false)
            return ReceivedViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_layout, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text = currMessage.text

        }else{
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.receivedMessage.text = currMessage.text
            viewHolder.senderUser.text = username
        }
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT

        }else{
            return ITEM_RECEIVED
        }
    }
    override fun getItemCount(): Int {

        return messageList.size
    }

    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.tvSentMessage)

    }
    class ReceivedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receivedMessage = itemView.findViewById<TextView>(R.id.tvReceivedMessage)
        val senderUser = itemView.findViewById<TextView>(R.id.tvUser)

    }
}