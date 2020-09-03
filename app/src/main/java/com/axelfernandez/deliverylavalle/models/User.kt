package com.axelfernandez.deliverylavalle.models

data class User(
    var username: String?,
    val email: String,
    val givenName: String,
    val familyName: String,
    val photo: String,
    var clientId: String?,
    var phone: String?
    )

data class UserResponse(
    val is_new :Boolean,
    val completeRegistry : Boolean,
    val clientId :String,
    val username :String,
    val access_token :String,
    val refresh_token :String
)

