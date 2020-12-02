package com.mmt.aidl

import android.os.Parcel
import android.os.Parcelable

/**
 * Copyright © 2020 妈妈团. All rights reserved.
 * @author yangchong
 * @version 1.0
 * @date 12/1/20 2:43 PM
 * @description
 */
open class Book(var name: String?, var price: String?) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Book(name=$name, price=$price)"
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }


}