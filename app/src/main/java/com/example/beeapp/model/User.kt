package com.example.beeapp.model

import java.util.*


data class User(
    var id: String,
    var username: String,
    var email: String,
    var password: String,
    var picture: String,
    var mood: String,
    var phone: String
) {


    //var contactUids: Set<String>? = null,


    //constructor() : this(null, "", "", "", "", "Not mood", "")
    constructor(id:String,username: String, email: String, password: String) :
            this(id, username, email, password, "", "Not mood", "")

    constructor(user: User) : this(
        user.id,
        user.username,
        user.email,
        user.password,
        user.picture,
        user.mood,
        user.phone
    )
}




