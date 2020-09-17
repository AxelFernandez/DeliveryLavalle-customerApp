package com.axelfernandez.deliverylavalle.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(
    private val api : Api

){
    val data = MutableLiveData<List<Product>>()


    fun getProduct(token :String,id: String): MutableLiveData<List<Product>> {
        api.getProductByCompanyId("Bearer %s".format(token), id).enqueue(object :
            Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                data.value = null
            }
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                data.value = response.body()

            }
        })
        return data

    }

    fun returnData(): LiveData<List<Product>> {
        return data
    }


}