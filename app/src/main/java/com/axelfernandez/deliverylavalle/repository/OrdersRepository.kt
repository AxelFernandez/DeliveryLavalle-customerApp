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
}