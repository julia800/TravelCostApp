package com.example.travelcostapp.module

import android.os.Parcel
import android.os.Parcelable

data class Expense(
    var typeOfExpense: String,
    var personsAffectedOfExpense: String,
    var personPayedExpense: String,
    var amount: Double,
    var tripId: String
) : Parcelable {

    constructor() : this("", "", "", 0.0, "")

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(typeOfExpense)
        parcel.writeString(personsAffectedOfExpense)
        parcel.writeString(personPayedExpense)
        parcel.writeDouble(amount)
        parcel.writeString(tripId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Expense> {
        override fun createFromParcel(parcel: Parcel): Expense {
            return Expense(parcel)
        }

        override fun newArray(size: Int): Array<Expense?> {
            return arrayOfNulls(size)
        }
    }
}
