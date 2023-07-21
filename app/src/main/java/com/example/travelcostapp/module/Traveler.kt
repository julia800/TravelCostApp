package com.example.travelcostapp.module
import android.os.Parcel
import android.os.Parcelable

data class Traveler(
    val firstName: String,
    val lastName: String,
    val expenses: List<SingleExpense>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(SingleExpense.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeTypedList(expenses)
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