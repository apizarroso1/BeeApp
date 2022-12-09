package com.example.beeapp.model

enum class EventType {
    LEISURE, BUSINESS, FAMILY, SPORTS;

      fun setType(t:String):EventType{

          return when(t){
              "LEISURE" -> LEISURE

              "BUSINESS" -> BUSINESS

              "FAMILY" -> FAMILY

              "SPORTS" -> SPORTS

              else -> LEISURE
          }

    }
}