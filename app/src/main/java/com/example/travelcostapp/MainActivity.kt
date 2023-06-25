package com.example.travelcostapp

import Trip
import TripAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var tripRecyclerView: RecyclerView
    private lateinit var tripAdapter: TripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tripRecyclerView = findViewById(R.id.tripRecyclerView)
        tripRecyclerView.layoutManager = LinearLayoutManager(this)
        tripAdapter = TripAdapter(getDummyTrips())
        tripRecyclerView.adapter = tripAdapter

        val createTripButton = findViewById<View>(R.id.createTripButton)
        createTripButton.setOnClickListener {
            val intent = Intent(this, CreateTripActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDummyTrips(): List<Trip> {
        // Create dummy trips for testing
        val trip1 = Trip("1", "Trip 1", "Destination 1", "2023-06-01", "2023-06-05")
        val trip2 = Trip("2", "Trip 2", "Destination 2", "2023-07-01", "2023-07-10")
        val trip3 = Trip("3", "Trip 3", "Destination 3", "2023-08-01", "2023-08-15")
        return listOf(trip1, trip2, trip3)
    }
}
