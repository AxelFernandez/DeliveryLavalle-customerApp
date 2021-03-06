package com.axelfernandez.deliverylavalle.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.AddressResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Singleton

@Singleton
class AddressRepository(
    private val api : Api
){
    val addressDeleted = MutableLiveData<String>()
    val addressLiveData = MutableLiveData<List<Address>>()
    val addressResponseLiveData = MutableLiveData<AddressResponse>()


    fun postAddress(address :Address): MutableLiveData<AddressResponse> {
        api.postAddress(address = address).enqueue(object : Callback<AddressResponse> {
            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                addressResponseLiveData.value = null
                return
            }
            override fun onResponse(call: Call<AddressResponse>, response: Response<AddressResponse>) {
                if (response.code() ==200) {
                    addressResponseLiveData.value = response.body()
                }
            }
        })
        return addressResponseLiveData

    }
    fun notifyPost(): LiveData<AddressResponse> {
        return addressResponseLiveData
    }

    fun getAllAddress(): MutableLiveData<List<Address>> {
        api.getAddress().enqueue(object : Callback<List<Address>> {
            override fun onFailure(call: Call<List<Address>>, t: Throwable) {
                addressLiveData.value = null
                return

            }
            override fun onResponse(call: Call<List<Address>>, response: Response<List<Address>>) {
                if (response.code() ==200){
                   addressLiveData.value = response.body()
                }
            }
        })
        return addressLiveData
    }

    fun notifyAddress(): LiveData<List<Address>>{
        return addressLiveData

    }

    fun deleteAddress(address: Address): MutableLiveData<String> {
        api.postAddressDelete(address).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                addressDeleted.value = null
                return

            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() ==200){
                    addressDeleted.value = response.body()
                }
            }
        })
        return addressDeleted
    }

    fun notifyAddressDeleted(): LiveData<String>{
        return addressDeleted
    }
}
