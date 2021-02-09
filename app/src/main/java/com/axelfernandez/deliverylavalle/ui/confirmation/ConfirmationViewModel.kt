package com.axelfernandez.deliverylavalle.ui.confirmation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Order
import com.axelfernandez.deliverylavalle.models.OrderPost
import com.axelfernandez.deliverylavalle.models.OrderResponse
import com.axelfernandez.deliverylavalle.repository.OrdersRepository
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import javax.inject.Inject

class ConfirmationViewModel : ViewModel() {

    lateinit var orderRepository :OrdersRepository

    fun getRepository(context: Context){
        orderRepository = OrdersRepository(RetrofitFactory.buildService(Api::class.java,context))
    }

    fun postOrder(order: OrderPost){
        orderRepository.postOrder(order)
    }

    fun returnOrders():LiveData<OrderResponse>{
        return orderRepository.returnResponse()
    }
}