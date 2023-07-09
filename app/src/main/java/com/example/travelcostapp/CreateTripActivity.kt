package com.example.travelcostapp

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.example.travelcostapp.module.Trip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateTripActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var daysEditText: EditText
    private lateinit var amountOfTravelersEditText: EditText
    private lateinit var createButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        nameEditText = findViewById(R.id.nameEditText)
        destinationEditText = findViewById(R.id.destinationEditText)
        daysEditText = findViewById(R.id.startDateEditText)
        amountOfTravelersEditText = findViewById(R.id.endDateEditText)
        createButton = findViewById(R.id.createButton)

        database = FirebaseDatabase.getInstance().getReference("trips")

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val destination = destinationEditText.text.toString()
            val days = daysEditText.text.toString()
            val amountOfTravelers = amountOfTravelersEditText.text.toString()

            if (validateInput(days, amountOfTravelers, name, destination)) {
                createNewTrip(name, destination, days.toInt(), amountOfTravelers.toInt())
                finish()
            }
        }
    }

    private fun validateInput(days: String, amountOfTravelers: String, name: String, destination: String): Boolean {

        if (TextUtils.isEmpty(name)) {
            nameEditText.error = "* Pflichtfeld"
            return false
        }

        if (TextUtils.isEmpty(destination)) {
            destinationEditText.error = "* Pflichtfeld"
            return false
        }

        if (TextUtils.isEmpty(days) || !isNumeric(days)) {
            daysEditText.error = "Bitte geben Sie eine gültige Anzahl an Tagen ein"
            return false
        }

        if (TextUtils.isEmpty(amountOfTravelers) || !isNumeric(amountOfTravelers)) {
            amountOfTravelersEditText.error = "Bitte geben Sie eine gültige Anzahl an Reisenden ein"
            return false
        }

        return true
    }

    private fun isNumeric(input: String): Boolean {
        val pattern = Regex("[0-9]+")
        return pattern.matches(input)
    }

    private fun createNewTrip(name: String, destination: String, days: Int, amountOfTravelers: Int) {
        val user = Trip(name, destination, days, amountOfTravelers)
        database.child(database.push().key!!).setValue(user)
    }
}
