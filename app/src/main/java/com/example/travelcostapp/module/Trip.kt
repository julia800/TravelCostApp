package com.example.travelcostapp.module

import android.os.Parcel
import android.os.Parcelable

data class Trip(
    val name: String,
    val destination: String,
    val days: Int,
    val amountOfTravelers: Int,
    val travelers: List<Traveler>
) : Parcelable {

    constructor() : this("", "", 0, 0, emptyList())

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(Traveler.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(destination)
        parcel.writeInt(days)
        parcel.writeInt(amountOfTravelers)
        parcel.writeTypedList(travelers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trip> {
        override fun createFromParcel(parcel: Parcel): Trip {
            return Trip(parcel)
        }

        override fun newArray(size: Int): Array<Trip?> {
            return arrayOfNulls(size)
        }
    }
}
