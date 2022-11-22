package com.example.beeapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.adapter.GroupAdapter
import com.example.beeapp.model.Event


class GroupsFragment : Fragment() {
    private lateinit var eventList: ArrayList<Event>

    private lateinit var adapter: GroupAdapter
   // private lateinit var auth: FirebaseAuth
    //private lateinit var dbRef: DatabaseReference
    private lateinit var rvGroups: RecyclerView
    private lateinit var btnGoCreateGroup: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_groups, container, false)
        //auth = FirebaseAuth.getInstance()
     //   dbRef =
     //       Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        eventList = ArrayList()
        adapter = GroupAdapter(requireContext(), eventList)
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

                startActivity(Intent(context, CreateGroupActivity::class.java))
            }
        }
    }


    private fun rvGroups() {


        /*dbRef.child("groups").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (postSnapshot in snapshot.children) {
                    val group = postSnapshot.getValue(Event::class.java)
                    if (group?.users!!.contains(auth.currentUser?.uid.toString())) {
                        eventList.add(group)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("rvGroups", "Something went wrong")
            }


        })*/

    }
}