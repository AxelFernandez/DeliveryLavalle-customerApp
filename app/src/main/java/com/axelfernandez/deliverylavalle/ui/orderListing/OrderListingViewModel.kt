package com.axelfernandez.deliverylavalle.ui.orderListing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Order
import com.axelfernandez.deliverylavalle.models.OrderPost
import com.axelfernandez.deliverylavalle.models.OrderResponse
import com.axelfernandez.deliverylavalle.repository.OrdersRepository
import javax.inject.Inject

class OrderListingViewModel : ViewModel() {

    @Inject
    private val orderRepository = OrdersRepository(RetrofitFactory.buildService(Api::class.java))

    fun getOrders(token : String){
        orderRepository.getOrders(token)
    }

    fun returnOrders():LiveData<List<Order>>{
        return orderRepository.returnOrders()
    }

    fun returnListOfOrders(list : List<Order>):Map<String,List<Order>>{
        val listInCourse = ArrayList<Order>()
        val listComplete = ArrayList<Order>()
        list.forEach {
            when (it.state){
                "Entregado" -> listComplete.add(it)
                "Cancelado" -> listComplete.add(it)
                else -> listInCourse.add(it)
            }
        }
        val result = HashMap<String,List<Order>>()
        result.put("inCourse",listInCourse)
        result.put("completed",listComplete)
        return result
    }
}