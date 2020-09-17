package com.axelfernandez.deliverylavalle.api

import com.axelfernandez.deliverylavalle.models.*
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


    //Company
    @GET("company_category")
    fun getCompanyCategories(@Header("Authorization") token: String):Call<List<CompanyCategoryResponse>>

    @POST("company")
    fun getCompany(@Header("Authorization") token: String, @Body location: Location):Call<List<Company>>


    //Detail Product
    @POST("product")
    fun getProductByCompanyId(@Header("Authorization") token: String, @Body id: String):Call<List<Product>>

    @POST("company_detail")
    fun getCompanyById(@Header("Authorization") token: String, @Body id: String):Call<Company>

}