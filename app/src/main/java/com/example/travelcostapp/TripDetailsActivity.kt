package com.example.travelcostapp

import android.app.AlertDialog
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
import com.example.travelcostapp.module.Expense
import com.example.travelcostapp.module.Trip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat

class TripDetailsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var headline: TextView
    private lateinit var subHeadline: TextView
    private lateinit var typeOfExpense : TextView
    private lateinit var affectedDropdown: TextView
    private lateinit var payedDropdown : TextView
    private lateinit var amountPayed : EditText
    private lateinit var saveButton : Button
    private lateinit var database: DatabaseReference

    private var trip: Trip? = null
    private var tripKey: String? = null
    private var langArray = arrayOf("Person 1", "Person 2", "Person 3")
    private var typeOfExpenseList = arrayOf("Flug", "Unterkunft", "Mietwagen", "Benzin",
        "Verpflegung", "Versicherung", "Transportkosten", "Medizinische Kosten", "Visum",
        "Zollgebühren", "Aktivitäten", "Eintrittsgelder", "Trinkgelder", "Sonstige Ausgaben")

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

        saveButton.setOnClickListener {
            val typeOfExpense = typeOfExpense.text.toString()
            val personsAffectedOfExpense = affectedDropdown.text.toString()
            val personPayedExpense = payedDropdown.text.toString()
            val amount = amountPayed.text.toString()
            val tripId = tripKey.toString()

            createNewExpense(typeOfExpense, personsAffectedOfExpense, personPayedExpense, amount, tripId)
            //TODO route to next page in pressed and worked
        }

        addToolbar()
        addHeadline()
        addTypeOfExpenseDropdown()
        addPersonAffectedDropdown()
        addPersonPayedDropdown()
        addAmountInputField()
    }

    private fun addToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun addHeadline() {
        trip = intent.getParcelableExtra<Trip>("trip")
        tripKey = intent.getStringExtra("tripKey")

        if (trip != null) {
            val travelers = trip?.travelers

            headline.text = trip?.name
            if (travelers != null) {
                langArray = travelers.map { it.firstName + " " + it.lastName.first() +"." }.toTypedArray()
            }
        }
    }

    private fun addTypeOfExpenseDropdown() {
        typeOfExpense.setOnClickListener {
            var selectedItem = -1

            val builder = AlertDialog.Builder(this@TripDetailsActivity)
            builder.setTitle("Art der Ausgabe")
            builder.setCancelable(false)
            builder.setSingleChoiceItems(typeOfExpenseList, selectedItem) { dialog, i ->
                selectedItem = i
            }

            builder.setPositiveButton("OK") { dialog, i ->
                if (selectedItem != -1) {
                    val selectedLang = typeOfExpenseList[selectedItem]
                    typeOfExpense.text = selectedLang
                    //TODO: selectedLang is String of result
                }
            }

            builder.setNegativeButton("Cancel") { dialog, i ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

    private fun addPersonAffectedDropdown() {
        affectedDropdown.setOnClickListener {
            val langList = mutableListOf<Int>()
            val selectedLanguage = BooleanArray(langArray.size)

            val builder = AlertDialog.Builder(this@TripDetailsActivity)
            builder.setTitle("Wer ist von den Kosten betroffen?")
            builder.setCancelable(false)
            builder.setMultiChoiceItems(langArray, selectedLanguage) { dialog, i, b ->
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
                    stringBuilder.append(langArray[langList[j]])
                    if (j != langList.size - 1) {
                        stringBuilder.append(", ")
                    }
                }
                affectedDropdown.text = stringBuilder.toString()
                //TODO: stringBuilder.toString() is List of result
            }

            builder.setNegativeButton("Cancel") { dialog, i ->
                dialog.dismiss()
            }

            builder.setNeutralButton("Clear All") { dialog, i ->
                for (j in selectedLanguage.indices) {
                    selectedLanguage[j] = false
                }
                langList.clear()
                affectedDropdown.text = ""
                //TODO: reset list of results
            }

            builder.show()
        }
    }

    private fun addPersonPayedDropdown() {
        payedDropdown.setOnClickListener {
            var selectedItem = -1

            val builder = AlertDialog.Builder(this@TripDetailsActivity)
            builder.setTitle("Wer hat gezahlt?")
            builder.setCancelable(false)
            builder.setSingleChoiceItems(langArray, selectedItem) { dialog, i ->
                selectedItem = i
            }

            builder.setPositiveButton("OK") { dialog, i ->
                if (selectedItem != -1) {
                    val selectedLang = langArray[selectedItem]
                    payedDropdown.text = selectedLang
                    //TODO: selectedLang is String of result
                }
            }

            builder.setNegativeButton("Cancel") { dialog, i ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

    private fun addAmountInputField() {
        var current = ""
        amountPayed.addTextChangedListener( object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    amountPayed.removeTextChangedListener(this)

                    val cleanString: String = s.replace("""[$,.]""".toRegex(), "")
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))

                    current = formatted
                    amountPayed.setText(formatted)
                    amountPayed.setSelection(formatted.length)
                    amountPayed.addTextChangedListener(this)
                }
            }
        })
    }

    private fun createNewExpense(typeOfExpense: String, personsAffectedOfExpense: String, personPayedExpense: String,
                                 amount: String, tripId: String) {

        var filteredExpense = amount.replace("""[$]""".toRegex(), "")
        val expense = Expense(typeOfExpense, personsAffectedOfExpense, personPayedExpense, filteredExpense.toDouble(), tripId)


        database = FirebaseDatabase.getInstance().getReference("Expenses")
        database.child(database.push().key!!).setValue(expense)
            .addOnSuccessListener {
                Toast.makeText(this, "Speicherung erfolgt", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Log.d("Fehler", "fehler: " + it.message)
            }
    }
}