package com.example.beeapp

data class Announcement(var id: String, var text: String, var author: User, var event: Event)
// TODO: tables call each other
