package com.example.beeapp.model

import java.util.*


data class User(
    var id: String,
    var username: String,
    var email: String,
    var password: String,
    var profilePicture: String? = null,
    var mood: String,
    var phone: String? = null,
) {


    //var contactUids: Set<String>? = null,


    //constructor() : this(null, "", "", "", "", "Not mood", "")
    constructor(uid:String,username: String, email: String, password: String) :
            this(uid, username, email, password, "", "Not mood", "")

    constructor(user: User) : this(
        user.id,
        user.username,
        user.email,
        user.password,
        user.profilePicture,
        user.mood,
        user.phone
    )
}




