package com.example.beeapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.R
import com.example.beeapp.activity.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.MessageAdapter
import com.example.beeapp.databinding.ActivityChatBinding
import com.example.beeapp.model.Chat
import com.example.beeapp.model.Message
import com.example.beeapp.service.*

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.StompMessage

import java.util.logging.Level
import java.util.logging.Logger


class ChatActivity : AppCompatActivity() {

    private lateinit var rvMessage: RecyclerView
    private lateinit var edMessage: EditText
    private lateinit var ivSendButton: ImageView
    private lateinit var viewBinding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>

    private lateinit var receiverId:String
    private lateinit var username:String

    private var apiChatInterface: ApiChatInterface = RetrofitService().getRetrofit().create()
    private lateinit var chat: Chat

    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address)

    @SuppressWarnings("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)



        receiverId = intent.getStringExtra("uid")!!
        username = intent.getStringExtra("username")!!
        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        initView()






        stompClient.connect()
        StompUtils.lifecycle(stompClient)

        //Subscripcion al endpoint para recibir mensaje

        stompClient.topic(Const.chatResponse.replace(Const.placeholder, loggedUser.id))
            .subscribe { stompMessage: StompMessage ->
                val jsonObject = JSONObject(stompMessage.payload)
                Log.i("SERVER", "Receive: $jsonObject")
                runOnUiThread {
                    try {
                        var message = Message(jsonObject.getString("senderId"),
                            jsonObject.getString("receiverId"),jsonObject.getString("body"))


                        messageList.add(message)


                        refreshChat()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }





        messageList= mutableListOf()

        apiChatInterface.findChat(loggedUser.id,receiverId ).enqueue(object :Callback<Chat>{
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
                }catch (e:Exception)
                {
                    Logger.getLogger("ChatError").log(Level.SEVERE, "${response.code()}",e)
                }

            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {
                Logger.getLogger("ChatError").log(Level.SEVERE, "Error trying to connect",t)
            }
        })




        messageAdapter = MessageAdapter(this, messageList)
        rvMessage.layoutManager = LinearLayoutManager(this)
        rvMessage.adapter = messageAdapter




        //añadimos el mensaje a la base de datos
        ivSendButton.setOnClickListener {
            sendMessage()
        }


    }



    private fun refreshChat() {

        messageAdapter.notifyItemInserted(messageList.size-1)
        try {
            rvMessage.smoothScrollToPosition(messageAdapter.itemCount - 1)
        } catch (e: Exception) {

        }

    }


    private fun sendMessage(){
        var message = edMessage.text.toString().trim()

        if(message!="") {

           // var messageObject = Message(loggedUser.id, receiverId, message)


            Logger.getLogger("CLIENT").log(Level.SEVERE, "message sent")
            val jsonObject = JSONObject()
            try {
                jsonObject.put("senderId", loggedUser.id)
                jsonObject.put("receiverId", receiverId)
                jsonObject.put("body", message)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            stompClient.send(Const.chat.replace(Const.placeholder, chat.id), jsonObject.toString()).subscribe()

            edMessage.setText("")
        }
    }



    private fun initView() {
        edMessage = viewBinding.edMessage
        rvMessage = viewBinding.rvMessage
        ivSendButton = viewBinding.ivSendButton
    }

    //Función que se encarga de crear la barra del menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Función que se encarga de las funciónes de cada elemento del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.viewContact -> {
                var intent = Intent(this, UserActivity::class.java)
                intent.putExtra("reference", receiverId)

                startActivity(intent)
            }
            android.R.id.home ->{
                onBackPressed()
                return true
            }

        }
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}