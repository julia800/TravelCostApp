package com.example.travelcostapp

import TripListView
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    private lateinit var tripRecyclerView: RecyclerView
    private lateinit var tripListView: TripListView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        tripRecyclerView = findViewById(R.id.tripRecyclerView)
        tripRecyclerView.layoutManager = LinearLayoutManager(this)
        tripListView = TripListView()
        tripRecyclerView.adapter = tripListView

        val createTripButton = findViewById<View>(R.id.createTripButton)
        createTripButton.setOnClickListener {
            val intent = Intent(this, CreateTripActivity::class.java)
            startActivity(intent)
        }
    }
}
