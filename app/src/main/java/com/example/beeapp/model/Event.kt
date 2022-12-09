package com.example.beeapp.model

import java.time.LocalDate
import java.time.LocalTime

data class Event(
    var id: String,
    var name: String? = null,
    var description: String? = null,
    var attendees: Set<String>?=null ,
    var date:LocalDate?=null,
    var time:LocalTime?=null,
    var location: String? = null,
    var type: EventType
)
