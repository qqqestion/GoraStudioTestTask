package ru.tashkent.gora.data.local

import android.os.Parcel
import android.os.Parcelable

data class UserView(
    val id: Int,
    val name: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeInt(id)
        parcel?.writeString(name)
    }

    companion object CREATOR : Parcelable.Creator<UserView> {
        override fun createFromParcel(parcel: Parcel): UserView {
            return UserView(parcel)
        }

        override fun newArray(size: Int): Array<UserView?> {
            return arrayOfNulls(size)
        }
    }
}