package com.axelfernandez.deliverylavalle.models

data class Address(
    var street: String,
    var number: String,
    var district: String,
    var floor: String?,
    var reference: String?,
    var location: String?
)

data class AddressResponse(
    val addressId :String
)

