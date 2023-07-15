package com.example.travelcostapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.travelcostapp.module.Trip

class TripDetailsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var headline: TextView
    private lateinit var affectedDropdown: TextView
    private lateinit var payedDropdown : TextView

    private var trip: Trip? = null
    private var langArray = arrayOf("Java", "C++", "Kotlin", "C", "Python", "Javascript")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        toolbar = findViewById(R.id.toolbar)
        headline = findViewById(R.id.headline)
        affectedDropdown = findViewById(R.id.textView)
        payedDropdown = findViewById(R.id.payedDropDown)

        addToolbar()
        addHeadline()
        addAffectedDropdown()
        addPayedDropdown()
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
        if (trip != null) {
            val travelers = trip?.travelers

            headline.text = trip?.name
            if (travelers != null) {
                langArray = travelers.map { it.firstName }.toTypedArray()
            }
        }
    }

    private fun addAffectedDropdown() {
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

    private fun addPayedDropdown() {
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
}
