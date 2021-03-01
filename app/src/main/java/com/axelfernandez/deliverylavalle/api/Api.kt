package com.axelfernandez.deliverylavalle.api

import com.axelfernandez.deliverylavalle.models.*
import retrofit2.Call;
import retrofit2.http.*

interface Api {

    @Headers("Content-Type: application/json")
    @POST("google")
    fun loginWithGoogle(@Body googleToken: String):Call<UserResponse>

    @POST("firebase_token")
    fun sendFirebaseToken(@Body firebaseToken: FirebaseToken):Call<ConfirmationObject>

    @PUT("firebase_token")
    fun deleteFirebaseToken(@Body firebaseToken: FirebaseToken):Call<ConfirmationObject>



    @GET("client")
    fun getClient(@Query("clientId") clientId:String):Call<User>

    @Headers("Content-Type: application/json")
    @POST("client")
    fun updatePhone(@Body userData: User):Call<User>

    @Headers("Content-Type: application/json")
    @GET("address")
    fun getAddress():Call<List<Address>>

    @Headers("Content-Type: application/json")
    @POST("address")
    fun postAddress(@Body address: Address):Call<AddressResponse>

    @Headers("Content-Type: application/json")
    @POST("address_delete")
    fun postAddressDelete(@Body address: Address):Call<String>


    //Company
    @GET("company_category")
    fun getCompanyCategories():Call<List<CompanyCategoryResponse>>

    @POST("company")
    fun getCompany(@Body location: Location):Call<List<Company>>


    @POST("payment_method")
    fun getPaymentMethodByCompanyId(@Body company: String):Call<PaymentMethods>

    @POST("delivery_method")
    fun getDeliveryMethodByCompanyId(@Body company: String):Call<PaymentMethods>

    //Detail Product
    @POST("product")
    fun getProductByCompanyId(@Body id: ProductRequest):Call<List<Product>>

    @POST("product_category")
    fun getProductCategoriesByCompanyId(@Body companyId: String):Call<List<ProductCategory>>

    @POST("company_detail")
    fun getCompanyById(@Body id: String):Call<Company>


    //Orders
    @POST("order")
    fun postOrder(@Body order: OrderPost):Call<OrderResponse>

    @GET("order")
    fun getOrders():Call<List<Order>>

    @POST("order_by_id")
    fun getOrdersById(@Body orderId: String):Call<Order>

    @POST("meli_link")
    fun getMeLiLink(@Body orderId: String):Call<MeliLink>

    @GET("rating")
    fun getRating(@Query("order")order : String):Call<Review>

    @POST("rating")
    fun postRating(@Body Review : Review):Call<String>

    @GET("reviews")
    fun getReviews(@Query("company") clientId:String):Call<List<Review>>

}