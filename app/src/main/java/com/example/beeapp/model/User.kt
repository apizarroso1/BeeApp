package com.example.beeapp.model

 data class User(
     var uid: String? = null ,
     var username: String? = null,
     var email: String? = null,
     var profilePicture: String? = null,
     var contactUids: Set<String>? = null


 )


