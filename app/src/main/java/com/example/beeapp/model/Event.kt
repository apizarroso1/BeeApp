package com.example.beeapp.model

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Event(
    var id: String,
    var name: String,
    var description: String? = null,
    var attendees: Set<String>? = null,
    var date: String? = null,
    var time: String? = null,
    var location: String? = null,
    var type: EventType,
    var chatId: String,
    var expenses: MutableList<Expense>
) {
    fun addExpense(expense: Expense) {
        this.expenses.add(expense)
    }

    fun removeExpese(expense: Expense) {
        this.expenses.remove(expense)
    }

    constructor(
        name: String,
        description: String,
        attendees: Set<String>,
        date:String,
        time:String,
        type: EventType,
        chatId: String
    ) : this(
        UUID.randomUUID().toString(), name, description, attendees, date, time, null, type, chatId,
        mutableListOf<Expense>()
    )
}
