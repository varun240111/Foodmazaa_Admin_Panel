package com.example.hotel.model

import android.os.Parcel
import android.os.Parcelable
import com.example.hotel.Pay_out

class OrderDetails():Parcelable{
    var userUid: String? = null
    var username: String? = null
    var foodName:MutableList<String>?=null
    var foodPrice:MutableList<String>?=null
    var foodQuantity:MutableList<Int>?=null
    var foodImage:MutableList<String>?=null

    var address: String? = null
    var totalPrice: String? = null
    var phoneNumber:String?=null
    var orderAccpted: Boolean=false
    var paymentRecevied: Boolean=false
    var itemPushkey: String? = null
    var currentTime: Long=0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        username = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccpted = parcel.readByte() != 0.toByte()
        paymentRecevied = parcel.readByte() != 0.toByte()
        itemPushkey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemQuantities: ArrayList<Int>,
        foodItemImage: ArrayList<String>,
        address: String,
        totalAmount: String,
        phone: String,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        time: Long
    ):this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(username)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccpted) 1 else 0)
        parcel.writeByte(if (paymentRecevied) 1 else 0)
        parcel.writeString(itemPushkey)
        parcel.writeLong(currentTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }


}
