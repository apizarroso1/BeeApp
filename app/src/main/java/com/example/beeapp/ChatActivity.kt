package com.example.beeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.MessageAdapter
import com.example.beeapp.databinding.ActivityChatBinding
import com.example.beeapp.model.Message
import com.example.beeapp.service.SocketListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var rvMessage: RecyclerView
    private lateinit var edMessage: EditText
    private lateinit var ivSendButton: ImageView
    private lateinit var viewBinding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
   // private lateinit var dbRef: DatabaseReference
    private lateinit var webSocket: WebSocket

   // var recieverRoom: String? = null
   // var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val username = intent.getStringExtra("username")
        val recieverUid = intent.getStringExtra("uid")
        //val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        //dbRef =
          //  Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference

        instantiateWebSocket ()

        //senderRoom = recieverUid + senderUid
        //recieverRoom = senderUid + recieverUid

        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList, username!!)
        rvMessage.layoutManager = LinearLayoutManager(this)
        rvMessage.adapter = messageAdapter


        //cargar los mensajes de un chat
        /*dbRef.child("chats").child(senderRoom!!).child("messages")
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

                }

            })*/

        //añadimos el mensaje a la base de datos
        ivSendButton.setOnClickListener {

            var message = edMessage.text.toString().trim()

            var msgId = UUID.randomUUID().toString();

            var messageObject = Message(msgId,loggedUser.id,recieverUid!!, message)

            /*dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    dbRef.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }*/



            edMessage.setText("")
        }


    }

    private fun initView() {
        edMessage = viewBinding.edMessage
        rvMessage = viewBinding.rvMessage
        ivSendButton = viewBinding.ivSendButton
    }

    private fun instantiateWebSocket (){
        var client = OkHttpClient()

        var request = Request.Builder().url("").build()
        var socketListener = SocketListener(this)
        webSocket = client.newWebSocket(request,socketListener)
    }

}