package com.example.beeapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.beeapp.R
import com.example.beeapp.databinding.ActivityExpensesBinding
import com.example.beeapp.databinding.ActivityUserBinding

class ExpensesActivity : AppCompatActivity() {


    private lateinit var viewBinding: ActivityExpensesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewBinding = ActivityExpensesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.title= "Expenses"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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