package com.example.travelcostapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.example.travelcostapp.module.Trip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateTripActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var createButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        nameEditText = findViewById(R.id.nameEditText)
        destinationEditText = findViewById(R.id.destinationEditText)
        startDateEditText = findViewById(R.id.startDateEditText)
        endDateEditText = findViewById(R.id.endDateEditText)
        createButton = findViewById(R.id.createButton)

        database = FirebaseDatabase.getInstance().getReference("trips")

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val destination = destinationEditText.text.toString()
            val startDate = startDateEditText.text.toString()
            val endDate = endDateEditText.text.toString()

            createNewTrip(name, destination, startDate, endDate)
            finish()
        }
    }

    fun createNewTrip(name: String, destination: String, startDate: String, endDate: String) {
        val user = Trip(name, destination, startDate,  endDate)
        database.child(database.push().key!!).setValue(user)
    }
}