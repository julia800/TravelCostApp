package com.example.travelcostapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TripDetailScreen : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var addButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_detail_screen)

        toolbar = findViewById(R.id.toolbar)
        addButton = findViewById(R.id.fabAdd)
        addButton.setOnClickListener {
            val intent = Intent(this, AddExpenseScreen::class.java)
            startActivity(intent)
        }
        addToolbar()
    }

    private fun addToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
