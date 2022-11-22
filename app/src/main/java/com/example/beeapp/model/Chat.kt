package com.example.beeapp.model

data class Chat(var id:String,var users:MutableSet<String>,var messages:MutableSet<String>) {
}