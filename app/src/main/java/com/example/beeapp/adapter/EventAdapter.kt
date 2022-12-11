package com.example.beeapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.activity.EventActivity
import com.example.beeapp.R
import com.example.beeapp.model.Event


class EventAdapter(val context: Context, val eventList: ArrayList<Event>) :
    RecyclerView.Adapter<EventAdapter.GroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.event_layout, parent, false)

        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val currentGroup = eventList[position]

        holder.tvGroupName.text = currentGroup.name

        //al pulsar en el card te lleva a ala activiada chat
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EventActivity::class.java)

            intent.putExtra("eventname", currentGroup.name)
            intent.putExtra("eventid", currentGroup.id)
            intent.putExtra("description",currentGroup.description.toString())

            var attendees:ArrayList<String> = ArrayList(currentGroup.attendees!!)

            intent.putStringArrayListExtra("attendees",attendees)

            Log.i("EVENT","$currentGroup")

            context.startActivity(intent)

        }
    }

    override fun getItemCount() = eventList.size

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvGroupName = itemView.findViewById<TextView>(R.id.tvGroupName)

    }
}