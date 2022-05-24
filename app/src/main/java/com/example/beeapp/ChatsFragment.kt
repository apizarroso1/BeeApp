package com.example.beeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ChatsFragment : Fragment() {
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var rvChats:RecyclerView
    companion object{
        private const val ARG_OBJECT = "object"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

         val view: View = inflater.inflate(R.layout.fragment_chats, container, false)
        rvChats = view.findViewById(R.id.rvChats)
        auth = FirebaseAuth.getInstance()
        dbRef = Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference


        userList = ArrayList()
        adapter = UserAdapter(requireContext(), userList)

        rvChats.layoutManager = LinearLayoutManager(requireContext())
        rvChats.adapter = adapter
        rvChats()
        return view


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        activity?.let {

        }

    }
    private fun rvChats(){
        dbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (auth.currentUser?.uid!= currentUser?.uid){
                        userList.add(currentUser!!)

                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}