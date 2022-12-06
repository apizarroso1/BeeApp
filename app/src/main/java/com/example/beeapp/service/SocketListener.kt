package com.example.beeapp.service

import com.example.beeapp.ChatActivity

import com.google.gson.JsonObject
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import java.util.logging.Level
import java.util.logging.Logger

class SocketListener:WebSocketListener {



    var activity:ChatActivity


    constructor( activity: ChatActivity){
        this.activity=activity
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        activity.runOnUiThread {
            Logger.getLogger("Connected").log(Level.SEVERE, " Connection Established")
        }
    }


    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        activity.runOnUiThread {
            var jsonObject = JsonObject()

            try {
                jsonObject.addProperty("message",text)
                //jsonObject.
            }catch (e: JSONException){

            }
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }

   /* override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
    }*/
}
