package com.example.beeapp.service

import android.os.Handler
import android.os.Looper
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

import java.util.logging.Level
import java.util.logging.Logger

class MyWebSocketClient(uri: URI):WebSocketClient(uri) {


    private var mUri: URI =uri
    var handler: Handler = Handler(Looper.getMainLooper())


    override fun onOpen(handshakedata: ServerHandshake?) {
        Logger.getLogger("Websocket").log(Level.SEVERE, "Opened")
        this.send("Hello from ")
    }

    override fun onMessage(message: String?) {
        handler.post {
            run {

            }
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {

    }

    override fun onError(ex: Exception?) {

    }
}