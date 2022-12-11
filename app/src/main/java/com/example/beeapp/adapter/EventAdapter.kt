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
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.event_layout, parent, false)

        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = eventList[position]

        holder.tvEventName.text = currentEvent.name

        //al pulsar en el card te lleva a ala activiada chat
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EventActivity::class.java)

            intent.putExtra("event",currentEvent)


            var attendees:ArrayList<String> = ArrayList(currentEvent.attendees!!)

            intent.putStringArrayListExtra("attendees",attendees)

            Log.i("EVENT","$currentEvent")

            context.startActivity(intent)

        }
    }

    override fun getItemCount() = eventList.size

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvEventName = itemView.findViewById<TextView>(R.id.tvEventName)

    }
}