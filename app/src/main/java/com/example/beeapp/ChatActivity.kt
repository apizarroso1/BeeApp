package com.example.beeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.MessageAdapter
import com.example.beeapp.databinding.ActivityChatBinding
import com.example.beeapp.model.Chat
import com.example.beeapp.model.Message
import com.example.beeapp.service.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.java_websocket.client.WebSocketClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.net.URI

import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var rvMessage: RecyclerView
    private lateinit var edMessage: EditText
    private lateinit var ivSendButton: ImageView
    private lateinit var viewBinding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var webSocket: WebSocket
    private lateinit var webSocketClient:WebSocketClient;
    private lateinit var receiverUid:String
    private lateinit var username:String

    private var apiChatInterface: ApiChatInterface = RetrofitService().getRetrofit().create()
    private lateinit var chat: Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        //val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        //dbRef =
          //  Firebase.database("https://beeapp-a567b-default-rtdb.europe-west1.firebasedatabase.app").reference

        //instantiateWebSocket ()

        receiverUid = intent.getStringExtra("uid")!!
        username = intent.getStringExtra("username")!!
        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        initView()

        messageList= ArrayList()



        apiChatInterface.findChat(loggedUser.id,receiverUid ).enqueue(object :Callback<Chat>{
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                try {
                    chat = response.body()!!

                    messageList.clear()
                    messageList.addAll(chat.messages)
                    messageAdapter.notifyDataSetChanged()
                    try {
                        rvMessage.smoothScrollToPosition(messageAdapter.itemCount - 1)
                    } catch (e: Exception) {

                    }
                    Logger.getLogger("ChatMessages").log(Level.SEVERE, "$messageList")
                }catch (e:Exception)
                {
                    Logger.getLogger("ChatError").log(Level.SEVERE, "${response.code()}",e)
                }

            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

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

            sendMessage()
        }


    }
    private fun connectWebSocket(){
        var uri: URI
        try{
            uri = URI("ws://192.168.1.67:8080/privatechat")
        }catch (e:Exception)
        {
            return
        }

        webSocketClient = MyWebSocketClient(uri)

        webSocketClient.connect()

    }

    private fun sendMessage(){
        var message = edMessage.text.toString().trim()

        var msgId = UUID.randomUUID().toString();

        var messageObject = Message(msgId,loggedUser.id,receiverUid!!, message)

        messageList.add(messageObject)



        chat.messages = messageList

        apiChatInterface.updateChat(chat).enqueue(object :Callback<Chat>{
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                if (response.code()==202){
                    Logger.getLogger("Message sent").log(Level.SEVERE, "code:${response.code()}")

                    messageList.clear()
                    messageList.addAll(response.body()!!.messages)
                    try {
                        rvMessage.smoothScrollToPosition(messageAdapter.itemCount - 1)
                    } catch (e: Exception) {

                    }
                    messageAdapter.notifyDataSetChanged()
                    Logger.getLogger("MessageList").log(Level.SEVERE, "$messageList")

                }else {

                    Logger.getLogger("Error").log(Level.SEVERE, "Couldn't send the message, code=${response.code()}")

                }
            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        /*dbRef.child("chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                dbRef.child("chats").child(recieverRoom!!).child("messages").push()
                    .setValue(messageObject)
            }*/


        edMessage.setText("")
    }


    private fun initView() {
        edMessage = viewBinding.edMessage
        rvMessage = viewBinding.rvMessage
        ivSendButton = viewBinding.ivSendButton
    }

    private fun instantiateWebSocket (){
        var client = OkHttpClient()

        var request = Request.Builder().url("ws://localhost:8080/privatechat").build()
        var socketListener = SocketListener(this)
        webSocket = client.newWebSocket(request,socketListener)
    }

}