package com.example.travelcostapp.module

data class Trip(
    val name: String = "",
    val destination: String = "",
    val days: Int = 0,
    val amountOfTravelers: Int = 0,
    val travelers: List<Traveler> = emptyList()
)

