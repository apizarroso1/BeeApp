package com.example.beeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.model.Group
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class GroupsFragment : Fragment() {
    private lateinit var userList: ArrayList<User>
    private lateinit var groupList: ArrayList<Group>

    private lateinit var adapter: GroupAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var rvGroups: RecyclerView
    private lateinit var btnGoCreateGroup: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_groups, container, false)
        auth = FirebaseAuth.getInstance()
        dbRef =
            Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference
        groupList = ArrayList()
        adapter = GroupAdapter(requireContext(), groupList)
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


        dbRef.child("groups").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()
                for (postSnapshot in snapshot.children) {
                    val group = postSnapshot.getValue(Group::class.java)
                    if (group?.users!!.contains(auth.currentUser?.uid.toString())) {
                        groupList.add(group)
                        Log.e("Lo que sea", groupList.toString())
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("rvGroups", "Something went wrong")
            }


        })

    }
}