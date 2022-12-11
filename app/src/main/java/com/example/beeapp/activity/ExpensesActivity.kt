package com.example.beeapp.activity

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beeapp.activity.EventActivity.Companion.event
import com.example.beeapp.adapter.AddContactAdapter
import com.example.beeapp.adapter.ExpenseAdapter
import com.example.beeapp.databinding.ActivityExpensesBinding
import com.example.beeapp.model.Event
import com.example.beeapp.model.Expense
import com.example.beeapp.service.ApiEventInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger

class ExpensesActivity : AppCompatActivity() {


    private lateinit var viewBinding: ActivityExpensesBinding
    private lateinit var dialog: AlertDialog
    private lateinit var btnAddExpenses: Button
    private lateinit var editPrice: EditText
    private lateinit var editName: EditText
    private lateinit var tvPpp: TextView
    lateinit var adapter: ExpenseAdapter
    private lateinit var rvExpenses: RecyclerView
    private var apiEventInterface: ApiEventInterface = RetrofitService().getRetrofit().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewBinding = ActivityExpensesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.title= "Expenses"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnAddExpenses = viewBinding.btnAddExpenses
        rvExpenses = viewBinding.rvExpenses
        tvPpp = viewBinding.tvPpp

        btnAddExpenses.setOnClickListener {
            addExpenses()
        }
        calcPpp()
        initRV()
    }
    private fun initRV() {


        rvExpenses.layoutManager = LinearLayoutManager(this)
        adapter = ExpenseAdapter(this, event.expenses)
        rvExpenses.adapter = adapter

    }

    private fun calcPpp() {
        var total = 0.0
        for(item in event.expenses){
            total += item.price.toDouble()
        }
        var ppp = total/ event.attendees!!.count()
        tvPpp.text = ppp.toString()+"€"
    }

    private fun addExpenses(){
        dialog = AlertDialog.Builder(this).create()
        dialog.setTitle("Add Expense")
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        editPrice = EditText(this)
        editPrice.setHint("Price")

        editName = EditText(this)
        editName.setHint("Expense")
        editPrice.setInputType(InputType.TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)
        linearLayout.addView(editName)
        linearLayout.addView(editPrice)


        editPrice.filters += InputFilter.LengthFilter(6)
        setEditDescriptionButton()
        dialog.setView(linearLayout)

        dialog.show()
    }
    private fun setEditDescriptionButton() {
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"Add"){ dialogInterface, i ->
            var updatedEvent= Event(EventActivity.event)

            val price = editPrice.text.toString()
            val name = editName.text.toString()
            var expense = Expense(name,price)
            updatedEvent.addExpense(expense)



            apiEventInterface.updateEvent(updatedEvent).enqueue(object: Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.code()==202){
                        Toast.makeText(
                            applicationContext,
                            "Expense added",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("ADDED").log(Level.INFO, "Expense added. code:${response.code()}")
                        calcPpp()
                    }else {
                        Toast.makeText(
                            applicationContext,
                            "Couldn't add the expense",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("NOT ADDED").log(Level.SEVERE, "Couldn't add the expense. code=${response.code()}")


                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to add the expense",t)
                }
            })

        }
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel"){ dialogInterface, i->
            dialog.dismiss()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home ->{
                onBackPressed()
                finish()
                return true
            }

        }
        return true
    }
}