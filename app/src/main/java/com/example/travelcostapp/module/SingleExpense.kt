package com.example.travelcostapp.module

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class SingleExpense(
    var amount: Double,
    var typeOfExpense: String,
    var payed: Boolean
) : Parcelable {
    
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readBoolean()
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(amount)
        parcel.writeString(typeOfExpense)
        parcel.writeBoolean(payed)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SingleExpense> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): SingleExpense {
            return SingleExpense(parcel)
        }

        override fun newArray(size: Int): Array<SingleExpense?> {
            return arrayOfNulls(size)
        }
    }
}
