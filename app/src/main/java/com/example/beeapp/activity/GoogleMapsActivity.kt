package com.example.beeapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.view.MenuItem
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
import com.example.beeapp.model.Message
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

    private var selectedLocation: String = ""
    private var previousLocation: String? = ""
    private var newLocation: String ="40.23300001572752;-3.3515353017628446"
    private lateinit var event: Event
    private lateinit var  btnSaveLocation: Button
    private lateinit var  btnExpenses: Button
    private var apiEventInterface: ApiEventInterface = RetrofitService().getRetrofit().create()

    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address)
    private lateinit var  eventId:String
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val eventName = intent.getStringExtra("eventName").toString()
        eventId = intent.getStringExtra("eventId").toString()

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



        btnSaveLocation.setOnClickListener {


            if(btnSaveLocation.text.contentEquals("change location",true)) {
                btnSaveLocation.text="save location"
                if (::map.isInitialized) {
                    map.setOnMapClickListener {
                        map.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)))
                        selectedLocation = "" + it.latitude + ";" + it.longitude
                        //video -> https://www.youtube.com/watch?v=_6EeTp4GxLo&ab_channel=Programaci%C3%B3nAndroidbyAristiDevs
                    }
                }
            }else{
                btnSaveLocation.text="change location"
            }
        }

        stompClient.connect()
        StompUtils.lifecycle(stompClient)


        /*stompClient.topic(Const.groupResponse.replace(Const.placeholder, eventId))
            .subscribe { stompMessage: StompMessage ->
                val jsonObject = JSONObject(stompMessage.payload)
                Log.i("SERVER", "Receive: $jsonObject")
                runOnUiThread {
                    try {
                        var message = Message(jsonObject.getString("senderId"),
                            jsonObject.getString("receiverId"),jsonObject.getString("body"))






                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }*/


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    private fun load(googleMap: GoogleMap){
        map = googleMap

        if(previousLocation?.isEmpty()==false) {
            val marker = LatLng(
                previousLocation!!.split(";")[0].toDouble(),
                previousLocation!!.split(";")[1].toDouble()
            )
            map.addMarker(MarkerOptions().position(marker).title("Marker in the abbyssm"))
            // Zoom 1world->20building
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 18f))
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        apiEventInterface.getEventById(eventId).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                try {
                    event = response.body()!!

                    previousLocation = event.location

                }catch (e:Exception)
                {
                    Logger.getLogger("EventError").log(Level.SEVERE, "${response.code()}",e)
                }

            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Logger.getLogger("EventError").log(Level.SEVERE, "Error trying to connect",t)
            }
        })
            load(googleMap)
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