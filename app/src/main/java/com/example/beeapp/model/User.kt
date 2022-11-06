package com.example.beeapp.model

import java.util.*


data class User(var uid: String?=null,var username: String,var email: String,var password: String) {

    //var profilePicture: String? = null,
    //var contactUids: Set<String>? = null,


    constructor() : this(null,"","","")
}




