package com.axelfernandez.deliverylavalle.api

import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.AddressResponse
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.models.UserResponse
import retrofit2.Call;
import retrofit2.http.*

interface Api {

    @Headers("Content-Type: application/json")
    @POST("google")
    fun loginWithGoogle(@Body userData: User):Call<UserResponse>


    @GET("client")
    fun getClient(@Query("clientId") clientId:String):Call<User>

    @Headers("Content-Type: application/json")
    @POST("client")
    fun updatePhone(@Body userData: User, @Header("Authorization") token: String):Call<User>

    @GET("address")
    fun getAddress():Call<List<Address>>

    @Headers("Content-Type: application/json")
    @POST("address")
    fun postAddress(@Body address: Address, @Header("Authorization") token: String):Call<AddressResponse>
}