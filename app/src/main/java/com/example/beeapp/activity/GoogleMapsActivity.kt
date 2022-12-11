package com.example.beeapp.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.util.Log.DEBUG
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.beeapp.BuildConfig.DEBUG
import com.example.beeapp.R
import com.example.beeapp.activity.EventActivity.Companion.event

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.beeapp.databinding.ActivityGoogleMapsBinding
import com.example.beeapp.model.Event
import com.example.beeapp.model.Message
import com.example.beeapp.model.User
import com.example.beeapp.service.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.StompMessage
import java.util.logging.Level
import java.util.logging.Logger


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    private lateinit var tvDescription:TextView
    private lateinit var tvHora:TextView
    private lateinit var tvFecha:TextView

    private var selectedLocation: String = ""
    private var previousLocation: String? = ""
    private var newLocation: String ="40.23300001572752;-3.3515353017628446"
    private lateinit var  btnSaveLocation: Button
    private lateinit var  btnExpenses: Button
    private var apiEventInterface: ApiEventInterface = RetrofitService().getRetrofit().create()
    private lateinit var dialog: AlertDialog
    private lateinit var editDescription: EditText

    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address)

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvDescription = binding.tvDescription
        tvHora = binding.tvHora
        tvFecha = binding.tvFecha
        tvHora.text = event.time
        tvFecha.text = event.date
        tvDescription.text = event.description

        supportActionBar?.title= "Mapa: ${event.name}"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSaveLocation = binding.btnSaveLocation
        btnExpenses = binding.btnExpenses
        previousLocation = event.location

        btnExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesActivity::class.java))
        }
        tvDescription.setOnClickListener {
            editDescription()
        }


        btnSaveLocation.setOnClickListener {


            if(btnSaveLocation.text.contentEquals("change location",true)) {
                btnSaveLocation.text="save location"
                if (::map.isInitialized) {
                    map.setOnMapClickListener {

                        selectedLocation = "" + it.latitude + ";" + it.longitude


                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 17f))
                        previousLocation = selectedLocation
                        onMapReady(map)
                    }
                }
            }else{
                btnSaveLocation.text="change location"
                changeLocation()
            }
        }

        stompClient.connect()
        StompUtils.lifecycle(stompClient)





        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun changeLocation() {
        event.location = selectedLocation
        apiEventInterface.updateEvent(event).enqueue(object :Callback<Event>{
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if(response.code()==202){
                    Log.i("LOCATION","Location changed")
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Log.i("LOCATION","ERROR trying to connect")
            }

        })
    }


    private fun editDescription(){
        dialog = AlertDialog.Builder(this).create()
        dialog.setTitle("Description")
        editDescription = EditText(this)
        editDescription.filters += InputFilter.LengthFilter(120)
        setEditDescriptionButton()
        dialog.setView(editDescription)
        editDescription.setText(tvDescription.text)
        dialog.show()
    }

    private fun setEditDescriptionButton() {
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"Save"){ dialogInterface, i ->
            var updatedEvent= Event(event)
            tvDescription.text = editDescription.text
            updatedEvent.description = editDescription.text.toString()



            apiEventInterface.updateEvent(updatedEvent).enqueue(object:Callback<Event>{
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.code()==202){
                        Toast.makeText(
                            applicationContext,
                            "Description changed",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("CHANGED").log(Level.INFO, "Description changed. code:${response.code()}")
                    }else {
                        Toast.makeText(
                            applicationContext,
                            "Couldn't change the description",
                            Toast.LENGTH_LONG
                        ).show()
                        Logger.getLogger("NOT CHANGED").log(Level.SEVERE, "Couldn't change the description. code=${response.code()}")


                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    Logger.getLogger("ERROR").log(Level.SEVERE, "Unexpected ERROR trying to change the mood",t)
                }
            })

        }
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel"){dialogInterface,i->
            dialog.dismiss()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {

        try {
            map.clear()
        }catch (e:Exception){

        }


        map = googleMap

        if(previousLocation?.isEmpty()==false) {
            val marker = LatLng(
                previousLocation!!.split(";")[0].toDouble(),
                previousLocation!!.split(";")[1].toDouble()
            )

            map.addMarker(MarkerOptions().position(marker).title("Marker in the abbyssm"))
            // Zoom 1world->20building
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 17f))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home ->{
                onBackPressed()
                return true
            }

        }
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}