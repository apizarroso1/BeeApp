package com.example.beeapp.model

import java.util.*


data class User(var uid: Int?=null,var username: String,var email: String,var password: String,var profilePicture: String? = null,var mood: String? = null,var phone: String? = null,) {


    //var contactUids: Set<String>? = null,


   // constructor() : this(null,"","","","","Not mood","")
    constructor(username: String,email: String,password: String):
            this(null,username,email,password,"","Not mood","")
}




