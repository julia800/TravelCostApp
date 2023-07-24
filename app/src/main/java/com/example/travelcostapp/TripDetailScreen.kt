package com.example.travelcostapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.travelcostapp.module.AllExpenses
import com.example.travelcostapp.module.ExpensePerPerson
import com.example.travelcostapp.module.Trip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.math.BigDecimal
import java.math.RoundingMode

class TripDetailScreen : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var addButton: FloatingActionButton
    private lateinit var headline: TextView
    private lateinit var subHeadline: TextView
    private lateinit var listViewExpenses: ListView
    private lateinit var listViewAllExpenses: ListView
    private var trip: Trip? = null
    private var tripKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_detail_screen)

        tripKey = intent.getStringExtra("tripKey")

        toolbar = findViewById(R.id.toolbar)
        headline = findViewById(R.id.headline)
        subHeadline = findViewById(R.id.subHeadline)
        listViewExpenses = findViewById(R.id.listViewExpenses)
        listViewAllExpenses = findViewById(R.id.listViewAllExpenses)
        addButton = findViewById(R.id.fabAdd)

        addToolbar()
        setupExpensesListView()
        setupAllExpensesListView()
        createAddButton()

        val database = FirebaseDatabase.getInstance()
        val tripRef = database.getReference("Trips").child(tripKey!!)

        tripRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trip = snapshot.getValue(Trip::class.java)

                if (trip != null) {
                    this@TripDetailScreen.trip = trip
                    setupExpensesListView()
                    setupAllExpensesListView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TripDetailScreen", "Data retrieval canceled: ${error.message}")
            }
        })
    }

    private fun addToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAddButton() {
        addButton.setOnClickListener {
            val intent = Intent(this, AddExpenseScreen::class.java)
            intent.putExtra("tripKey", tripKey)
            intent.putExtra("trip", trip)
            startActivity(intent)
        }
    }

    private fun setupExpensesListView() {
        val individualExpensesList = mutableListOf<String>()
        val listOfExpensePerPerson = AllExpenses(expenses = ArrayList<ExpensePerPerson>())

        headline.text = "Gesamt Kosten: ${calculateTotalCost(trip)}"

        trip?.travelers?.forEach { traveler ->
            traveler.expenses.forEachIndexed { index, expense ->
                val name = "${traveler.firstName} ${traveler.lastName.first()}."
                listOfExpensePerPerson.expenses.add(ExpensePerPerson(index, name, expense))
            }
        }

        val groupedExpenses: Map<Int, List<ExpensePerPerson>> = listOfExpensePerPerson.expenses.groupBy { it.id }
        var personPayed = ""

        for ((id, expenses) in groupedExpenses) {
            expenses.forEach {expense ->
                val payed = expense.expense.payed
                val amount = expense.expense.amount
                var payee = false

                payee = !(amount == 0.0 && payed)

                if (payee && payed) {
                    personPayed = expense.name
                }
            }

            for (expensePerPerson in expenses) {
                val name = expensePerPerson.name
                val payed = expensePerPerson.expense.payed
                val amount = expensePerPerson.expense.amount
                val type = expensePerPerson.expense.typeOfExpense

                if(!payed) {
                    individualExpensesList.add("$name schuldet $personPayed ${roundDouble(amount, 2)}€ für $type")
                }
            }
        }

        individualExpensesList.sort()
        val individualExpensesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, individualExpensesList)
        listViewExpenses.adapter = individualExpensesAdapter
    }

    private fun setupAllExpensesListView() {
        val listOfExpensePerPerson = AllExpenses(expenses = ArrayList<ExpensePerPerson>())
        val listOfExpenses = ArrayList<String>()

        headline.text = "Gesamt Kosten für ${trip?.name}: ${calculateTotalCost(trip)}€"
        subHeadline.text = "Offene Schulden"

        trip?.travelers?.forEach { traveler ->
            traveler.expenses.forEachIndexed { index, expense ->
                val name = "${traveler.firstName} ${traveler.lastName.first()}."
                listOfExpensePerPerson.expenses.add(ExpensePerPerson(index, name, expense))
            }
        }

        val groupedExpenses: Map<String, List<ExpensePerPerson>> = listOfExpensePerPerson.expenses.groupBy { it.expense.typeOfExpense }
        for ((id, expenses) in groupedExpenses) {
            var amountTotal = 0.0
            for (expensePerPerson in expenses) {
                val amount = expensePerPerson.expense.amount
                amountTotal += amount
            }
            listOfExpenses.add("${id}: ${roundDouble(amountTotal, 2)}€")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfExpenses)
        listViewAllExpenses.adapter = adapter
    }

    private fun calculateTotalCost(trip: Trip?): Double {
        var totalCost = 0.0
        trip?.travelers?.forEach { traveler ->
            traveler.expenses.forEach { expense ->
                totalCost += expense.amount
            }
        }
        return roundDouble(totalCost, 2)
    }

    private fun roundDouble(value: Double, decimalPlaces: Int): Double {
        if (decimalPlaces < 0) throw IllegalArgumentException("Decimal places must be non-negative.")

        val bigDecimal = BigDecimal(value.toString())
        return bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP).toDouble()
    }
}