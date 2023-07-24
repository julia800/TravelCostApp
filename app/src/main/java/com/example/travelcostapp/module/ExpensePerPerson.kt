package com.example.travelcostapp.module

data class ExpensePerPerson(
    val id: Int,
    val name: String,
    var expense: SingleExpense
)