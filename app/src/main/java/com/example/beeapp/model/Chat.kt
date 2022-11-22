package com.example.beeapp.model

data class Chat(var chatId:String,var users:MutableSet<String>,var messages:MutableSet<String>) {
}