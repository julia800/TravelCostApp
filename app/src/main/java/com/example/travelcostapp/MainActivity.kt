package com.example.travelcostapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.travelcostapp.R.*
import com.example.travelcostapp.module.Trip
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var listView: ListView
    private lateinit var database: DatabaseReference
    private lateinit var tripList: MutableList<Trip>
    private var mapOfTrips: MutableMap<String, Trip> = mutableMapOf()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        database = FirebaseDatabase.getInstance().reference.child("Trips")

        toolbar = findViewById(id.toolbar)

        listView = findViewById(id.listView)
        tripList = mutableListOf()
        createListView()

        val createTripButton = findViewById<View>(id.createTripButton)
        createTripButton.setOnClickListener {
            val intent = Intent(this, CreateTripActivity::class.java)
            startActivity(intent)
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val tripKey = mapOfTrips.keys.elementAt(position)
            val trip = mapOfTrips[tripKey]
            if (trip != null) {
                openTripDetails(tripKey, trip)
            } else {
                Log.e("MainActivity", "Trip not found in list")
            }
        }
    }

    private fun createListView() {
        val imgAdapter = ImageListAdapter(this, tripList)
        listView.adapter = imgAdapter

        val tripListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tripList.clear()
                for (tripSnapshot in dataSnapshot.children) {
                    val trip = tripSnapshot.getValue(Trip::class.java)
                    trip?.let {
                        tripList.add(it)
                        mapOfTrips.put(tripSnapshot.key.toString(), it)
                    }
                }
                imgAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        database.addValueEventListener(tripListener)
    }

    private fun openTripDetails(tripKey: String, trip: Trip) {
        val intent = Intent(this, AddExpenseScreen::class.java)
        intent.putExtra("tripKey", tripKey)
        intent.putExtra("trip", trip)
        startActivity(intent)
    }
}
