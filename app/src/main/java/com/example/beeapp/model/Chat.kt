package com.example.beeapp.model

data class Chat(var id:String,var users:Set<String>,var messages:List<Message>, var type: ChatType) {
}