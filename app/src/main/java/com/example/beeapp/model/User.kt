package com.example.beeapp.model


import java.util.*



data class User(
    var id: String,
    var username: String,
    var email: String,
    var password: String,
    var mood: String,
    var contacts: MutableSet<String>,
    var picture: ByteArray?=null

) {






    constructor(id:String,username: String, email: String, password: String) :
            this(id, username, email, password, "Not mood", HashSet<String>(), null)

    constructor(user: User) : this(
        user.id,
        user.username,
        user.email,
        user.password,
        user.mood,
        HashSet<String>(),
        user.picture

    )

    override fun toString(): String {
        return "User(id='$id', username='$username', email='$email', password='$password', mood='$mood', contacts=$contacts)"
    }
    fun addContact(newContactId:String){
        this.contacts.add(newContactId)
    }

}




