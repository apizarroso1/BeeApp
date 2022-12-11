package com.example.beeapp.service;

public class Const {
    public static final String TAG = "Alex";
    public static  final String placeholder = "placeholder";




    // la direccion para conectar al websocket del server
    // public static final String address = "ws://192.168.1.67:8080/beeapp/websocket";
     public static final String address = "ws://192.168.1.67:8080/beeapp/websocket";

    //El placeholder será reemplazado por el groupId correspondiente
    public static final String event = "/event/" + placeholder;
    public static final String eventResponse = "/e/" + placeholder;
    public static final String chat = "/chat/"+placeholder;
    // El placeholder será reemplazado por el userId correspondiente
    public static final String chatResponse = "/user/" + placeholder + "/msg";
}
