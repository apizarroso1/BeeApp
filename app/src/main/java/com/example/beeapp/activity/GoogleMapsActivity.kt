package com.example.beeapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.beeapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.beeapp.databinding.ActivityGoogleMapsBinding

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    private lateinit var tvDescription:TextView

    private var selectedLocation: String = ""
    private lateinit var  btnSaveLocation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val groupName = intent.getStringExtra("groupname").toString()

        val description = intent.getStringExtra("description").toString()

        tvDescription = binding.tvDescription
        tvDescription.text = description
        supportActionBar?.title= "Mapa: $groupName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSaveLocation = findViewById(R.id.btnSaveLocation)
        btnSaveLocation.setOnClickListener {
            if (::map.isInitialized){
                map.setOnMapClickListener{
                    map.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("Selected location"))
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
        val villablanca = LatLng(40.40681014415026, -3.5999011699561745)
        map.addMarker(MarkerOptions().position(villablanca).title("Marker in Villablanca Highschool"))
        // Zoom 1world->20building
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(villablanca, 20f))
    }
}