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
    private lateinit var textView: TextView
    private lateinit var selected: TextView

    private var trip: Trip? = null
    var langArray = arrayOf("Java", "C++", "Kotlin", "C", "Python", "Javascript")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        toolbar = findViewById(R.id.toolbar)
        headline = findViewById(R.id.headline)
        textView = findViewById(R.id.textView);
        selected = findViewById(R.id.selected)

        addToolbar()
        addHeadline()
        addDropdown()
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
            headline.text = trip?.name
        }
    }

    private fun addDropdown() {
        textView.setOnClickListener {
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
                textView.text = stringBuilder.toString()
                selected.text = stringBuilder.toString()
            }

            builder.setNegativeButton("Cancel") { dialog, i ->
                dialog.dismiss()
            }

            builder.setNeutralButton("Clear All") { dialog, i ->
                for (j in selectedLanguage.indices) {
                    selectedLanguage[j] = false
                }
                langList.clear()
                textView.text = ""
                selected.text = ""
            }

            builder.show()
        }
    }
}
