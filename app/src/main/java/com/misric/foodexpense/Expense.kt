package com.misric.foodexpense

import android.os.Parcel
import android.os.Parcelable

class Expense() : Parcelable {
    var Id: Int? = null
    var Year: Int? = null
    var Month: Int? = null
    var Date: String? = null
    var Category: String? = null
    var Note: String? = null
    var Avatar: String? = null
    var Amount: Float? = null

    constructor(parcel: Parcel) : this() {
        Id = parcel.readValue(Int::class.java.classLoader) as? Int
        Year = parcel.readValue(Int::class.java.classLoader) as? Int
        Month = parcel.readValue(Int::class.java.classLoader) as? Int
        Date = parcel.readString()
        Category = parcel.readString()
        Note = parcel.readString()
        Avatar = parcel.readString()
        Amount = parcel.readValue(Float::class.java.classLoader) as? Float
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(Id)
        parcel.writeValue(Year)
        parcel.writeValue(Month)
        parcel.writeString(Date)
        parcel.writeString(Category)
        parcel.writeString(Note)
        parcel.writeString(Avatar)
        parcel.writeValue(Amount)
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