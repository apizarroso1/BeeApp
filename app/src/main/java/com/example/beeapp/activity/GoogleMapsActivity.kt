package com.example.beeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.widget.Button
import android.widget.TextView
import com.example.beeapp.BuildConfig.DEBUG
import com.example.beeapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.beeapp.databinding.ActivityGoogleMapsBinding
import com.example.beeapp.model.Event
import com.example.beeapp.service.ApiChatInterface
import com.example.beeapp.service.ApiEventInterface
import com.example.beeapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.logging.Level
import java.util.logging.Logger

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    private lateinit var tvDescription:TextView

    private var selectedLocation: String = ""
    private var previousLocation: String = ""
    private var newLocation: String ="40.23300001572752;-3.3515353017628446"
    private lateinit var event: Event
    private lateinit var  btnSaveLocation: Button
    private lateinit var  btnExpenses: Button
    private var apiEventInterface: ApiEventInterface = RetrofitService().getRetrofit().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val eventName = intent.getStringExtra("eventName").toString()
        val eventId = intent.getStringExtra("eventId").toString()

        val description = intent.getStringExtra("description").toString()

        tvDescription = binding.tvDescription
        tvDescription.text = description
        supportActionBar?.title= "Mapa: $eventName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSaveLocation = binding.btnSaveLocation
        btnExpenses = binding.btnExpenses

        btnExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesActivity::class.java))
        }

        apiEventInterface.getEventById(eventId ).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                try {
                    event = response.body()!!

                    previousLocation = event.location.toString()

                }catch (e:Exception)
                {
                    Logger.getLogger("EventError").log(Level.SEVERE, "${response.code()}",e)
                }

            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Logger.getLogger("EventError").log(Level.SEVERE, "Error trying to connect",t)
            }
        })

        btnSaveLocation.setOnClickListener {
            if (::map.isInitialized){
                map.setOnMapClickListener{
                    map.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)))
                    selectedLocation = "" + it.latitude + ";" + it.longitude
                    //video -> https://www.youtube.com/watch?v=_6EeTp4GxLo&ab_channel=Programaci%C3%B3nAndroidbyAristiDevs
                }
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val marker = LatLng(previousLocation.split(";")[0].toDouble(), previousLocation.split(";")[1].toDouble())
        map.addMarker(MarkerOptions().position(marker).title("Marker in the abbyssm"))
        // Zoom 1world->20building
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 18f))
    }
}