package com.example.travelcostapp

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.travelcostapp.module.Traveler
import com.example.travelcostapp.module.Trip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateTripActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var daysEditText: EditText
    private lateinit var amountOfTravelersEditText: EditText
    private lateinit var createButton: Button
    private lateinit var personNameLayout: LinearLayout
    private lateinit var travelersFirstNameEditText: EditText
    private lateinit var travelersLastNameEditText: EditText
    private lateinit var database: DatabaseReference
    private lateinit var headline: TextView
    private var validatedName: Boolean = true

    private var personCount: Int = 0
    private var input: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        nameEditText = findViewById(R.id.nameEditText)
        destinationEditText = findViewById(R.id.destinationEditText)
        daysEditText = findViewById(R.id.startDateEditText)
        amountOfTravelersEditText = findViewById(R.id.endDateEditText)
        personNameLayout = findViewById(R.id.personNameLayout)
        headline = findViewById(R.id.headlineTextView)
        createButton = findViewById(R.id.createButton)

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val destination = destinationEditText.text.toString()
            val days = daysEditText.text.toString()
            val amountOfTravelers = amountOfTravelersEditText.text.toString()
            val travelers = getTravelersFromEditTexts()

            if (validateInput(days, amountOfTravelers, name, destination) && validatedName) {
                createNewTrip(name, destination, days.toInt(), amountOfTravelers.toInt(), travelers)
                finish()
            }
        }

        amountOfTravelersEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                input = s.toString()
                addTravelerInputFields(getAmountOfTraveler(input))

                if (!s.isNullOrBlank()) {
                    val amountOfTravelers = s.toString()
                    if (amountOfTravelers != "" && amountOfTravelers != "0") {
                        headline.text = "Reisende"
                        addTravelerInputFields(getAmountOfTraveler(input))
                    } else {
                        headline.text = ""
                        personNameLayout.removeAllViews()
                    }
                } else {
                    headline.text = ""
                    personNameLayout.removeAllViews()
                }
            }
        })
    }

    private fun getAmountOfTraveler(amount: String): Int {
        return if (amount.isEmpty())
            0
        else
            amount.toInt()
    }

    private fun getTravelersFromEditTexts(): List<Traveler> {
        val travelersList = mutableListOf<Traveler>()

        for (i in 0 until personNameLayout.childCount) {

            val personLayout = personNameLayout.getChildAt(i) as LinearLayout
            val firstNameEditText = personLayout.findViewById<EditText>(R.id.personEditText)
            val lastNameEditText = personLayout.findViewById<EditText>(R.id.lastNameEditText)

            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()

            if (firstName == "") {
                validatedName = false
                firstNameEditText.error = "* Pflichtfeld"
            } else if (lastName == "") {
                validatedName = false
                lastNameEditText.error = "* Pflichtfeld"
            } else {
                validatedName = true
                travelersList.add(Traveler(firstName, lastName))
            }
        }
        return travelersList
    }

    private fun addTravelerInputFields(count: Int) {
        val inflater = LayoutInflater.from(this)
        personNameLayout.removeAllViews()

        for (i in 0 until count) {
            val personLayout = inflater.inflate(R.layout.add_travelers, null) as? LinearLayout

            travelersFirstNameEditText = personLayout?.findViewById<EditText>(R.id.personEditText)!!
            travelersLastNameEditText = personLayout.findViewById<EditText>(R.id.lastNameEditText)!!

            travelersFirstNameEditText.hint = "Firstname ${personCount + i + 1}"
            travelersLastNameEditText.hint = "Lastname"

            personNameLayout.addView(personLayout)
        }
        personCount = count
    }

    private fun validateInput(
        days: String, amountOfTravelers: String, name: String, destination: String): Boolean {

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

    private fun createNewTrip(name: String, destination: String, days: Int,
        amountOfTravelers: Int, travelers: List<Traveler>) {
        val trip = Trip(name, destination, days, amountOfTravelers, travelers)

        database = FirebaseDatabase.getInstance().getReference("Trips")
        database.child(database.push().key!!).setValue(trip)
            .addOnSuccessListener {
                Toast.makeText(this, "Speicherung erfolgt", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Log.d("Fehler", "fehler: " + it.message)
            }
    }
}
