package com.example.beeapp.model

data class Event(
    var groupId: String? = null,
    var gName: String? = null,
    var gDescription: String? = null,
    var users: MutableMap<String, String>? = HashMap(),
    var ubication: String? = null
)
