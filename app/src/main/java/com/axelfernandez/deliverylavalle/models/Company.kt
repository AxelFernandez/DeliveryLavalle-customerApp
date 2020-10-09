package com.axelfernandez.deliverylavalle.models

data class Company(
    val id :String,
    val name :String,
    val description :String,
    val photo :String,
    val address :String,
    val methods :List<String>,
    val category :String?
)

data class CompanyCategoryResponse(
    val description :String,
    val photo :String
)
data class PaymentMethods(
    val methods :List<String>
)

//This class is for request the near company
data class Location(
    val lat :String,
    val long :String,
    val category :String?
)