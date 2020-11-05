package com.axelfernandez.deliverylavalle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.models.ConfirmationObject
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Singleton

@Singleton
class LoginRepository (
    private val api : Api
){
    val data = MutableLiveData<UserResponse>()

     fun getToken(user: User): MutableLiveData<UserResponse> {
        api.loginWithGoogle(user).enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                data.value = null

            }
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                data.value = response.body()
                data.postValue(response.body())

            }
        })
         return data
    }

    fun returnData(): LiveData<UserResponse>{
        return data
    }

    val tokenLiveData = MutableLiveData<ConfirmationObject>()

    fun sendToken(token: String, firebaseToken:String): MutableLiveData<ConfirmationObject> {
        api.sendFirebaseToken(firebaseToken, "Bearer %s".format(token)).enqueue(object : Callback<ConfirmationObject> {
            override fun onFailure(call: Call<ConfirmationObject>, t: Throwable) {
                tokenLiveData.value = null

            }
            override fun onResponse(call: Call<ConfirmationObject>, response: Response<ConfirmationObject>) {
                tokenLiveData.value = response.body()
            }
        })
        return tokenLiveData
    }

    fun returnFirebaseResponse(): LiveData<ConfirmationObject>{
        return tokenLiveData
    }

}