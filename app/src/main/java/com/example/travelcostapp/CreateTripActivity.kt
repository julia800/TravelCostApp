package com.example.travelcostapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class CreateTripActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        nameEditText = findViewById(R.id.nameEditText)
        destinationEditText = findViewById(R.id.destinationEditText)
        startDateEditText = findViewById(R.id.startDateEditText)
        endDateEditText = findViewById(R.id.endDateEditText)
        createButton = findViewById(R.id.createButton)

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val destination = destinationEditText.text.toString()
            val startDate = startDateEditText.text.toString()
            val endDate = endDateEditText.text.toString()

            // TODO: Save the trip details to database or perform desired action

            // Finish the activity and go back to com.example.myapplication.MainActivity
            finish()
        }
    }
}
