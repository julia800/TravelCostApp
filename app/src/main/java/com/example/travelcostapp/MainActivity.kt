package com.example.travelcostapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
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
            openTripDetails(tripList[position])
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
                    }
                }
                imgAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        database.addValueEventListener(tripListener)
    }

    private fun openTripDetails(trip: Trip) {
        val intent = Intent(this, TripDetailsActivity::class.java)
        intent.putExtra("trip", trip)
        startActivity(intent)
    }
}
