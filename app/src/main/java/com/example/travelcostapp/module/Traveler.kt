package com.example.travelcostapp.module
import android.os.Parcel
import android.os.Parcelable

data class Traveler(
    val firstName: String,
    val lastName: String
) : Parcelable {

    constructor() : this("", "")

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Traveler> {
        override fun createFromParcel(parcel: Parcel): Traveler {
            return Traveler(parcel)
        }

        override fun newArray(size: Int): Array<Traveler?> {
            return arrayOfNulls(size)
        }
    }
}