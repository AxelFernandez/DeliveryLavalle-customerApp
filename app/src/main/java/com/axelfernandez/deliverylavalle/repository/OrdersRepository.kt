package com.axelfernandez.deliverylavalle.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersRepository (
    private val api : Api

){
    val orders = MutableLiveData<List<Order>>()
    val rating = MutableLiveData<Review>()
    val reviews = MutableLiveData<List<Review>>()
    val data = MutableLiveData<String>()
    val ordersById = MutableLiveData<Order>()
    var orderPost = MutableLiveData<OrderResponse>()
    var meliLink = MutableLiveData<MeliLink>()


    fun getOrders(token :String): MutableLiveData<List<Order>> {
        api.getOrders("Bearer %s".format(token)).enqueue(object : Callback<List<Order>> {
            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                orders.value = null
            }
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                orders.value = response.body()
            }
        })
        return orders

    }

    fun returnOrders(): LiveData<List<Order>> {
        return orders
    }

    fun returnResponse(): LiveData<OrderResponse> {
        return orderPost
    }

    fun postOrder(order : OrderPost, token: String): MutableLiveData<OrderResponse> {
        api.postOrder("Bearer %s".format(token),order).enqueue(object :
            Callback<OrderResponse> {
            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                orderPost.value = null
            }
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                    orderPost.value = response.body()
            }
        })
        return orderPost
    }


    fun returnOrdersById(): LiveData<Order> {
        return ordersById
    }

    fun getOrdersById(orderId : String,token :String): MutableLiveData<Order> {

        api.getOrdersById("Bearer %s".format(token),orderId).enqueue(object : Callback<Order> {
            override fun onFailure(call: Call<Order>, t: Throwable) {
                ordersById.value = null
            }
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                ordersById.value = response.body()
            }
        })
        return ordersById

    }


    fun returnMeliLink(): LiveData<MeliLink> {
        return meliLink
    }

    fun getMeliLinkByOrderId(orderId : String,token :String): MutableLiveData<MeliLink> {
        api.getMeLiLink("Bearer %s".format(token),orderId).enqueue(object : Callback<MeliLink> {
            override fun onFailure(call: Call<MeliLink>, t: Throwable) {
                meliLink.value = null
            }
            override fun onResponse(call: Call<MeliLink>, response: Response<MeliLink>) {
                meliLink.value = response.body()
            }
        })
        return meliLink

    }


    fun returnRating(): LiveData<Review> {
        return rating
    }

    fun getRating(orderId : String,token :String): MutableLiveData<Review> {
        api.getRating("Bearer %s".format(token),orderId).enqueue(object : Callback<Review> {
            override fun onFailure(call: Call<Review>, t: Throwable) {
                rating.value = null
            }
            override fun onResponse(call: Call<Review>, response: Response<Review>) {
                rating.value = response.body()
            }
        })
        return rating
    }

    fun postRating(token :String, review:Review): MutableLiveData<String> {
        api.postRating("Bearer %s".format(token),review).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                data.value = null
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                data.value = response.body()
            }
        })
        return data
    }

    fun returnResponseRating(): LiveData<String> {
        return data
    }

    fun getReviews(token : String,companyId :String): MutableLiveData<List<Review>> {
        api.getReviews("Bearer %s".format(token),companyId).enqueue(object : Callback<List<Review>> {
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                reviews.value = null
            }
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                reviews.value = response.body()
            }
        })
        return reviews
    }
    fun returnReviews(): LiveData<List<Review>> {
        return reviews
    }
}