package com.axelfernandez.deliverylavalle.repository

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.User
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class ClientRepository (
    private val api : Api

){
    val data = MutableLiveData<User>()


    fun getClient(clientId :String):MutableLiveData<User>{
        api.getClient(clientId).enqueue(object : Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                data.value = null
            }
            override fun onResponse(call: Call<User>, response: Response<User>) {
                data.value = response.body()

            }
        })
        return data

    }

    fun returnData():LiveData<User>{
        return data
    }

    fun updatePhone(user :User, token: String):MutableLiveData<User>{
        api.updatePhone(userData = user,token = "Bearer %s".format(token)).enqueue(object : Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                data.value = null
            }
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() ==200){
                data.value = response.body()
                data.postValue(response.body())
                }
            }
        })
        return data
    }
}