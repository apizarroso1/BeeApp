package com.example.beeapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.activity.CreateEventActivity
import com.example.beeapp.activity.LoginActivity.Companion.loggedUser
import com.example.beeapp.R
import com.example.beeapp.adapter.EventAdapter
import com.example.beeapp.model.Event
import com.example.beeapp.service.ApiEventInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger


class EventsFragment : Fragment() {
    private lateinit var eventList: ArrayList<Event>

    private lateinit var adapter: EventAdapter

    private lateinit var rvGroups: RecyclerView
    private lateinit var btnGoCreateGroup: Button
    private var apiEventInterface: ApiEventInterface = RetrofitService().getRetrofit().create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_events, container, false)


        eventList = ArrayList()
        adapter = EventAdapter(requireContext(), eventList)
        rvGroups = view.findViewById(R.id.rvGroups)
        btnGoCreateGroup = view.findViewById(R.id.btnGoCreateGroup)

        rvGroups.layoutManager = LinearLayoutManager(requireContext())
        rvGroups.adapter = adapter
        rvGroups()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let {


            btnGoCreateGroup.setOnClickListener {

                startActivity(Intent(context, CreateEventActivity::class.java))

            }
        }
    }

    override fun onResume() {
        super.onResume()

        rvGroups()
    }

    private fun rvGroups() {

        apiEventInterface.findAllEventsFromUser(loggedUser.id)
            .enqueue(object : Callback<List<Event>> {
                override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                    Logger.getLogger("rvGroups")
                        .log(Level.INFO, "Events=${response.body()}, code=${response.code()}")

                    eventList.clear()
                    eventList.addAll(response.body()!!)

                    adapter.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                    Logger.getLogger("rvGroups").log(Level.SEVERE, "Error connecting", t)
                }

            })



    }
}