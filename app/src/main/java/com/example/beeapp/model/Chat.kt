package com.example.beeapp.model

data class Chat(var id:String,var users:HashSet<String>,var messages:ArrayList<Message>) {
}