package com.axelfernandez.deliverylavalle.ui.cellphone

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.repository.ClientRepository
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class CellPhoneViewModel() : ViewModel() {


    @Inject
    val clientRepository = ClientRepository(RetrofitFactory.buildService(Api::class.java))




    fun getUserData(context: Context): User{
        return LoginUtils.getUserFromSharedPreferences(context)
    }

    fun startUpdatePhone(context: Context, phone:String, token: String){
        val user : User = LoginUtils.getUserFromSharedPreferences(context)
        user.phone = phone
        LoginUtils.putUserToSharedPreferences(context = context,new_user = user)
        clientRepository.updatePhone(user = user ,token = token)
    }
    fun getClientUpdated():LiveData<User>{
        return clientRepository.returnData()
    }


}


