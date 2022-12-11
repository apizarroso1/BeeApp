package com.example.beeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.R
import com.example.beeapp.activity.EventActivity
import com.example.beeapp.activity.EventActivity.Companion.event
import com.example.beeapp.activity.LoginActivity
import com.example.beeapp.model.*
import com.example.beeapp.service.ApiChatInterface
import com.example.beeapp.service.ApiEventInterface
import com.example.beeapp.service.ApiUserInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.exp

class ExpenseAdapter(val context: Context, private val expenses: MutableList<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpensesViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpensesViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.expense_layout, parent, false)

        return ExpensesViewHolder(view)
    }


    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {


        holder.tvExpensePrice.text = expenses[position].price+"€"
        holder.tvExpenseName.text = expenses[position].name

    }


    override fun getItemCount() = expenses.size

    class ExpensesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvExpenseName = itemView.findViewById<TextView>(R.id.tvExpenseName)
        val tvExpensePrice = itemView.findViewById<TextView>(R.id.tvExpensePrice)
    }

}