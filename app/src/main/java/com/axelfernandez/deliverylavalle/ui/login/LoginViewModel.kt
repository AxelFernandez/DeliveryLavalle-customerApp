package com.axelfernandez.deliverylavalle.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.FirebaseToken
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.repository.ProductRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class LoginViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    @Inject
    private val loginRepository = LoginRepository(RetrofitFactory.buildService(Api::class.java))


    fun sendFirebaseToken(token:String,firebase:FirebaseToken){
        loginRepository.sendToken(token, firebase)
    }

    fun loginWithGoogle(token: String){
        loginRepository.getToken(token)
    }

    fun returnToken(): LiveData<UserResponse> {
        return loginRepository.returnData()
    }
}