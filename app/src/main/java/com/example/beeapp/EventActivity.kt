package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.MessageAdapter
import com.example.beeapp.databinding.ActivityEventBinding
import com.example.beeapp.model.Message

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EventActivity : AppCompatActivity() {
    private lateinit var rvMessage: RecyclerView
    private lateinit var edMessage: EditText
    private lateinit var ivSendButton: ImageView
    private lateinit var viewBinding: ActivityEventBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var usernameList: HashMap<String,String>
   // private lateinit var dbRef: DatabaseReference
    private lateinit var groupName:String
    private lateinit var description:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)


        viewBinding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

         groupName = intent.getStringExtra("eventname").toString()
        val eventId = intent.getStringExtra("eventid").toString()
        description = intent.getStringExtra("description").toString()
       // val senderUid = FirebaseAuth.getInstance().currentUser?.uid
       // dbRef =
    //        Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference


        supportActionBar?.title = groupName
        initView()
        messageList = ArrayList()
        usernameList = getUsernames(eventId!!)
        messageAdapter = MessageAdapter(this, messageList, usernameList)
        rvMessage.layoutManager = LinearLayoutManager(this)
        rvMessage.adapter = messageAdapter


        //cargar los mensajes de un chat
       /* dbRef.child("groupchats").child(groupId!!).child("messages")
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

            })*/

        //añadimos el mensaje a la base de datos
        ivSendButton.setOnClickListener {
            var message = edMessage.text.toString().trim()

            var msgId = UUID.randomUUID().toString();

            var messageObject = Message(loggedUser.id,eventId, message)

           /* dbRef.child("groupchats").child(groupId!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    edMessage.setText("")
                }*/

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun getUsernames(groupId: String): HashMap<String,String> {

        var usernames = HashMap<String,String>()

       /* dbRef.child("groups").child(groupId).child("users")
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

            })*/

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
                intent.putExtra("description",description)
                startActivity(intent)
                finish()
            }
            android.R.id.home ->{
                onBackPressed()
                return true
            }

        }
        return true
    }
}