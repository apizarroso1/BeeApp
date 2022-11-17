package com.example.beeapp.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger


data class User(
    var id: String,
    var username: String,
    var email: String,
    var password: String,
    var mood: String,
    var phone: String,
    var picture: ByteArray?=null

) {


    //var contactUids: Set<String>? = null,



    constructor(id:String,username: String, email: String, password: String) :
            this(id, username, email, password, "Not mood", "", null)

    constructor(user: User) : this(
        user.id,
        user.username,
        user.email,
        user.password,
        user.mood,
        user.phone,
        user.picture

    )
}




