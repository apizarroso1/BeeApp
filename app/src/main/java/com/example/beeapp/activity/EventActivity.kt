package com.example.beeapp.activity

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.beeapp.R
import com.example.beeapp.activity.LoginActivity.Companion.loggedUser
import com.example.beeapp.adapter.MessageAdapter
import com.example.beeapp.databinding.ActivityEventBinding
import com.example.beeapp.model.Chat
import com.example.beeapp.model.Event
import com.example.beeapp.model.Message
import com.example.beeapp.model.User
import com.example.beeapp.service.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.StompMessage

import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.HashMap

class EventActivity : AppCompatActivity() {
    private lateinit var rvMessage: RecyclerView
    private lateinit var edMessage: EditText
    private lateinit var ivSendButton: ImageView
    private lateinit var viewBinding: ActivityEventBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>
    private var usernameList: HashMap<String,String> = HashMap()

   // private lateinit var event: Event
    private lateinit var chat: Chat

    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address)

    private var apiChatInterface: ApiChatInterface = RetrofitService().getRetrofit().create()
    private var apiUserInterface: ApiUserInterface = RetrofitService().getRetrofit().create()

    companion object{
        lateinit var event:Event
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)


        viewBinding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        event = intent.getSerializableExtra("event") as Event



        //supportActionBar?.title = eventName
        supportActionBar?.title = event.name
        initView()


        messageList= mutableListOf()


        getUsernames()
        rvMessage = viewBinding.rvMessage
        rvMessage.layoutManager = LinearLayoutManager(this)

        messageAdapter = MessageAdapter(this, messageList, usernameList)
        rvMessage.adapter = messageAdapter

        stompClient.connect()
        StompUtils.lifecycle(stompClient)

        stompClient.topic(Const.eventResponse.replace(Const.placeholder, event.id))
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
        //cargar los mensajes de un chat
        apiChatInterface.getChatById(event.chatId).enqueue(object :Callback<Chat>{
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



        //añadimos el mensaje a la base de datos
        ivSendButton.setOnClickListener {
            sendMessage()

        }
    }

    private fun getUsernames(){


        apiUserInterface.findContacts(event.attendees!!.toSet()).enqueue(object : Callback<List<User>> {

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                var userList = response.body()!!

                for (user: User in userList) {

                    usernameList[user.id] = user.username
                    messageAdapter.notifyDataSetChanged()
                }
                Log.i("USERLISTR","$usernameList")


            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {

            }
        })




    }

    private fun initView() {
        edMessage = viewBinding.edMessage

        ivSendButton = viewBinding.ivSendButton
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


            Logger.getLogger("CLIENT").log(Level.SEVERE, "message sent")
            val jsonObject = JSONObject()
            try {
                jsonObject.put("senderId", loggedUser.id)
                jsonObject.put("receiverId", event.id)
                jsonObject.put("body", message)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            stompClient.send(Const.event.replace(Const.placeholder, event.id), jsonObject.toString()).subscribe()

            edMessage.setText("")
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.goToMap -> {
                val intent = Intent(this, GoogleMapsActivity::class.java)

                intent.putExtra("event",event)
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