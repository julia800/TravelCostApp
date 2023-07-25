package com.example.travelcostapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.travelcostapp.module.SingleExpense
import com.example.travelcostapp.module.Traveler
import com.example.travelcostapp.module.Trip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
class AddExpenseScreen : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var headline: TextView
    private lateinit var subHeadline: TextView
    private lateinit var typeOfExpense: TextView
    private lateinit var affectedDropdown: TextView
    private lateinit var payedDropdown: TextView
    private lateinit var amountPayed: EditText
    private lateinit var saveButton: Button
    private lateinit var database: DatabaseReference
    private var travelers: List<Traveler> = listOf()

    private var trip: Trip? = null
    private var tripKey: String? = null
    private var allTravelersArray = arrayOf("Person 1", "Person 2", "Person 3")
    private var typeOfExpenseList = arrayOf(
        "Flug", "Unterkunft", "Mietwagen", "Benzin",
        "Verpflegung", "Versicherung", "Transportkosten", "Medizinische Kosten", "Visum",
        "Zollgebühren", "Aktivitäten", "Eintrittsgelder", "Trinkgelder", "Sonstige Ausgaben"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        toolbar = findViewById(R.id.toolbar)
        headline = findViewById(R.id.headline)
        subHeadline = findViewById(R.id.subHeadline)
        typeOfExpense = findViewById(R.id.typeOfExpense)
        affectedDropdown = findViewById(R.id.textView)
        payedDropdown = findViewById(R.id.payedDropDown)
        amountPayed = findViewById(R.id.amountPayed)
        saveButton = findViewById(R.id.saveButton)

        addToolbar()
        addHeadline()
        addTypeOfExpenseDropdown()
        addPersonAffectedDropdown()
        addPersonPayedDropdown()
        addAmountInputField()

        saveButton.setOnClickListener {
            val typeOfExpense = typeOfExpense.text.toString()
            val personsAffectedOfExpense = affectedDropdown.text.toString()
            val personPayedExpense = payedDropdown.text.toString()
            val amount = amountPayed.text.toString()
            val tripId = tripKey.toString()

            createNewExpense(
                typeOfExpense,
                personsAffectedOfExpense,
                personPayedExpense,
                amount,
                tripId
            )
            val intent = Intent(this, TripDetailScreen::class.java)
            intent.putExtra("tripKey", tripKey)
            intent.putExtra("trip", trip)
            startActivity(intent)
        }
    }

    private fun addToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, TripDetailScreen::class.java)
            intent.putExtra("tripKey", tripKey)
            intent.putExtra("trip", trip)
            startActivity(intent)
        }
    }

    private fun addHeadline() {
        trip = intent.getParcelableExtra<Trip>("trip")
        tripKey = intent.getStringExtra("tripKey")

        if (trip != null) {
            travelers = trip!!.travelers

            headline.text = trip?.name
            allTravelersArray =
                travelers.map { it.firstName + " " + it.lastName.first() + "." }.toTypedArray()
        }
    }

    private fun addTypeOfExpenseDropdown() {
        typeOfExpense.setOnClickListener {
            var selectedItem = -1

            val builder = AlertDialog.Builder(this@AddExpenseScreen)
            builder.setTitle("Art der Ausgabe")
            builder.setCancelable(false)
            builder.setSingleChoiceItems(typeOfExpenseList, selectedItem) { dialog, i ->
                selectedItem = i
            }

            builder.setPositiveButton("OK") { dialog, i ->
                if (selectedItem != -1) {
                    val selectedLang = typeOfExpenseList[selectedItem]
                    typeOfExpense.text = selectedLang
                }
            }

            builder.setNegativeButton("Abbrechen") { dialog, i ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

    private fun addPersonAffectedDropdown() {
        affectedDropdown.setOnClickListener {
            val langList = mutableListOf<Int>()
            val selectedLanguage = BooleanArray(allTravelersArray.size)

            val builder = AlertDialog.Builder(this@AddExpenseScreen)
            builder.setTitle("Wer ist von den Kosten betroffen?")
            builder.setCancelable(false)
            builder.setMultiChoiceItems(allTravelersArray, selectedLanguage) { dialog, i, b ->
                if (b) {
                    langList.add(i)
                    langList.sort()
                } else {
                    langList.remove(i)
                }
            }

            builder.setPositiveButton("OK") { dialog, i ->
                val stringBuilder = StringBuilder()
                for (j in langList.indices) {
                    stringBuilder.append(allTravelersArray[langList[j]])
                    if (j != langList.size - 1) {
                        stringBuilder.append(", ")
                    }
                }
                affectedDropdown.text = stringBuilder.toString()
            }

            builder.setNegativeButton("Abbrechen") { dialog, i ->
                dialog.dismiss()
            }

            builder.setNeutralButton("Auswahl löschen") { dialog, i ->
                for (j in selectedLanguage.indices) {
                    selectedLanguage[j] = false
                }
                langList.clear()
                affectedDropdown.text = ""
            }

            builder.show()
        }
    }

    private fun addPersonPayedDropdown() {
        payedDropdown.setOnClickListener {
            var selectedItem = -1

            val builder = AlertDialog.Builder(this@AddExpenseScreen)
            builder.setTitle("Wer hat gezahlt?")
            builder.setCancelable(false)
            builder.setSingleChoiceItems(allTravelersArray, selectedItem) { dialog, i ->
                selectedItem = i
            }

            builder.setPositiveButton("OK") { dialog, i ->
                if (selectedItem != -1) {
                    val selectedLang = allTravelersArray[selectedItem]
                    payedDropdown.text = selectedLang
                }
            }

            builder.setNegativeButton("Abbrechen") { dialog, i ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

    private fun addAmountInputField() {
        var current = ""
        val decimalFormatSymbols = DecimalFormatSymbols.getInstance().apply {
            currencySymbol = "€"
            groupingSeparator = '.'
        }
        val decimalFormat = DecimalFormat("#,##0.00", decimalFormatSymbols)

        amountPayed.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    amountPayed.removeTextChangedListener(this)

                    val cleanString: String = s.replace("""[€,.]""".toRegex(), "")
                    val parsed = cleanString.toDouble() / 100 // Divide by 100 because we are working with cents
                    val formatted = decimalFormat.format(parsed)

                    current = formatted
                    amountPayed.setText(formatted)
                    amountPayed.setSelection(formatted.length)
                    amountPayed.addTextChangedListener(this)
                }
            }
        })
    }

    private fun createNewExpense(
        typeOfExpense: String, personsAffectedOfExpense: String,
        personPayedExpense: String, amount: String, tripId: String
    ) {
        val filteredExpense = amount.replace("""[$]""".toRegex(), "")

        for (traveler in travelers) {
            val amountOfAffectedTravelers = personsAffectedOfExpense.count { it == ',' } + 1
            val currentTraveler = traveler.firstName + " " + traveler.lastName.first() + "."
            val amountPerPerson = filteredExpense.toDouble() / amountOfAffectedTravelers

            if (currentTraveler == personPayedExpense) {
                val index = travelers.indexOf(traveler)
                safeExpense(tripId, amountPerPerson, typeOfExpense, index, true)
            } else if (personsAffectedOfExpense.contains(currentTraveler)) {
                val index = travelers.indexOf(traveler)
                safeExpense(tripId, amountPerPerson, typeOfExpense, index, false)
            } else {
                val index = travelers.indexOf(traveler)
                safeExpense(tripId, 0.0, typeOfExpense, index, true)
            }
        }
    }

    private fun safeExpense(
        tripId: String,
        amount: Double,
        typeOfExpense: String,
        indexOfTraveler: Int,
        payed: Boolean
    ) {
        database = FirebaseDatabase.getInstance().getReference("Trips")
        val selectedTrip = database.child(tripId).child("travelers")

        selectedTrip
            .child(indexOfTraveler.toString())
            .child("expenses")
            .child("${trip!!.travelers[0].expenses.size}")
            .setValue(SingleExpense(amount, typeOfExpense, payed))

            .addOnSuccessListener {
                Toast.makeText(this, "Speicherung erfolgt", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Log.d("Fehler", "fehler: " + it.message)
            }
    }
}
