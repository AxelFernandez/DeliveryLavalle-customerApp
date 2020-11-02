package com.axelfernandez.deliverylavalle.ui.confirmation

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

    @Inject
    private val orderRepository = OrdersRepository(RetrofitFactory.buildService(Api::class.java))

    fun postOrder(order: OrderPost, token : String){
        orderRepository.postOrder(order,token)
    }

    fun returnOrders():LiveData<OrderResponse>{
        return orderRepository.returnResponse()
    }
}