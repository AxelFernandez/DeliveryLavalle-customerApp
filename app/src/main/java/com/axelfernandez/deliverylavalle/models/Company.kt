package com.axelfernandez.deliverylavalle.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
    val id :String,
    val name :String,
    val description :String,
    val photo :String,
    val rating :Float,
    val phone :String,
    val address :String,
    val methods :List<String>,
    val category :String?
): Parcelable

data class CompanyCategoryResponse(
    val description :String,
    val photo :String
)
//User in Delivery Method too
//TODO: Change the name for Method
data class PaymentMethods(
    val methods :List<String>
)

//This class is for request the near company
data class Location(
    val lat :String,
    val long :String,
    val category :String?
)