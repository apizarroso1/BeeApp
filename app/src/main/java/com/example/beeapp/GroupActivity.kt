package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.databinding.ActivityGroupBinding
import com.example.beeapp.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GroupActivity : AppCompatActivity() {
    private lateinit var rvMessage: RecyclerView
    private lateinit var edMessage: EditText
    private lateinit var ivSendButton: ImageView
    private lateinit var viewBinding: ActivityGroupBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var usernameList: HashMap<String,String>
    private lateinit var dbRef: DatabaseReference
    private lateinit var groupName:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)


        viewBinding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

         groupName = intent.getStringExtra("groupname").toString()
        val groupId = intent.getStringExtra("groupid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef =
            Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference


        supportActionBar?.title = groupName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        messageList = ArrayList()
        usernameList = getUsernames(groupId!!)
        messageAdapter = MessageAdapter(this, messageList, null, usernameList)
        rvMessage.layoutManager = LinearLayoutManager(this)
        rvMessage.adapter = messageAdapter


        //cargar los mensajes de un chat
        dbRef.child("groupchats").child(groupId!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children) {

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    try {
                        rvMessage.smoothScrollToPosition(messageAdapter.itemCount - 1)
                    } catch (e: Exception) {

                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ERROR", "Something went wrong")
                }

            })

        //añadimos el mensaje a la base de datos
        ivSendButton.setOnClickListener {

            val message = edMessage.text.toString().trim()
            val messageObject = Message(senderUid, message)

            dbRef.child("groupchats").child(groupId!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    edMessage.setText("")
                }

        }
    }

    private fun getUsernames(groupId: String): HashMap<String,String> {

        var usernames = HashMap<String,String>()

        dbRef.child("groups").child(groupId).child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (postSnapshot in snapshot.children) {

                        usernames[postSnapshot.key.toString()] = postSnapshot.getValue(String::class.java).toString()

                    }
                    Log.e("Usernames", usernames.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ERROR", "Something went wrong")
                }

            })

        return usernames

    }

    private fun initView() {
        edMessage = viewBinding.edMessage
        rvMessage = viewBinding.rvMessage
        ivSendButton = viewBinding.ivSendButton
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.goToMap -> {
                val intent = Intent(this, GoogleMapsActivity::class.java)
                intent.putExtra("groupname",groupName)
                startActivity(intent)
                finish()
            }
        }
        return true
    }
}