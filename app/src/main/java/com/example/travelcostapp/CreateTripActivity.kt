package com.example.travelcostapp

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.travelcostapp.module.Travelers
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
    private lateinit var personNameLayout: LinearLayout
    private lateinit var headline: TextView
    private var personCount: Int = 0
    private var input: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        nameEditText = findViewById(R.id.nameEditText)
        destinationEditText = findViewById(R.id.destinationEditText)
        daysEditText = findViewById(R.id.startDateEditText)
        amountOfTravelersEditText = findViewById(R.id.endDateEditText)
        amountOfTravelersEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                input = s.toString()
                addPersonEditTexts(getAmountOfTraveler(input))
            }
        })
        personNameLayout = findViewById(R.id.personNameLayout)
        headline = findViewById(R.id.headlineTextView)
        createButton = findViewById(R.id.createButton)
        database = FirebaseDatabase.getInstance().getReference("trips")

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val destination = destinationEditText.text.toString()
            val days = daysEditText.text.toString()
            val amountOfTravelers = amountOfTravelersEditText.text.toString()
            val travelers = listOf(Travelers("0", "2", ""))

            if (validateInput(days, amountOfTravelers, name, destination, travelers)) {
                createNewTrip(name, destination, days.toInt(), amountOfTravelers.toInt(), travelers)
                finish()
            }
        }
    }

    private fun getAmountOfTraveler(amount: String): Int {
        return if (amount.isEmpty())
            0
        else
            amount.toInt()
    }

    private fun addPersonEditTexts(count: Int) {
        val inflater = LayoutInflater.from(this)
        personNameLayout.removeAllViews()

        for (i in 0 until count) {
            val personLayout = inflater.inflate(R.layout.item_person, null) as? LinearLayout
            val personEditText = personLayout?.findViewById<EditText>(R.id.personEditText)
            val lastNameEditText = personLayout?.findViewById<EditText>(R.id.lastNameEditText)

            personEditText?.hint = "Firstname ${personCount + i + 1}"
            lastNameEditText?.hint = "Lastname"

            personNameLayout.addView(personLayout)
        }
        personCount = count
    }

    private fun validateInput(
        days: String,
        amountOfTravelers: String,
        name: String,
        destination: String,
        travelers: List<Travelers>
    ): Boolean {

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

    private fun createNewTrip(
        name: String,
        destination: String,
        days: Int,
        amountOfTravelers: Int,
        travelers: List<Travelers>
    ) {
        val user = Trip(name, destination, days, amountOfTravelers, travelers)
        database.child(database.push().key!!).setValue(user)
    }
}
