package com.example.travelcostapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.travelcostapp.module.Trip
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TripDetailsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var headline: TextView
    private lateinit var selected: TextView
    private var trip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        toolbar = findViewById(R.id.toolbar)
        headline = findViewById(R.id.headline)
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
        val items = listOf("Material", "Detail", "Components", "Android")
        val autoComplete: AutoCompleteTextView = findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(this, R.layout.item_list, items)
        autoComplete.setAdapter(adapter)

        val chipGroup: ChipGroup = findViewById(R.id.chip_group)
        val selectedItems = mutableSetOf<String>()

        autoComplete.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            if (selectedItem != null && !selectedItems.contains(selectedItem)) {
                selectedItems.add(selectedItem)
                val chip = Chip(this)
                chip.text = selectedItem
                chip.isCloseIconVisible = true
                chip.setOnCloseIconClickListener {
                    selectedItems.remove(selectedItem)
                    chipGroup.removeView(chip)
                }
                chipGroup.addView(chip)
                autoComplete.text = null
            }
        }
    }
}
