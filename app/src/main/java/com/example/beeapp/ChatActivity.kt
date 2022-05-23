package com.example.beeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.databinding.ActivityChatBinding
import com.example.beeapp.databinding.ActivityMainBinding
import com.example.beeapp.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var rvMessage: RecyclerView
    private lateinit var edMessage: EditText
    private lateinit var ivSendButton: ImageView
    private lateinit var viewBinding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var dbRef: DatabaseReference

    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val username = intent.getStringExtra("username")
        val recieverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference



        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid

                supportActionBar?.title = username

        initView()

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        rvMessage.layoutManager = LinearLayoutManager(this)
        rvMessage.adapter = messageAdapter

        //
        dbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }

                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //añadimos el mensaje a la base de datos
        ivSendButton.setOnClickListener{

            val message = edMessage.text.toString().trim()
            val messageObject = Message(senderUid,message)

            dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    dbRef.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            edMessage.setText("")
        }


    }

    private fun initView() {
        edMessage = viewBinding.edMessage
        rvMessage = viewBinding.rvMessage
        ivSendButton = viewBinding.ivSendButton


    }
}