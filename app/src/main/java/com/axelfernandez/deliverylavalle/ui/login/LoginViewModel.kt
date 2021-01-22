package com.axelfernandez.deliverylavalle.ui.login

import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.FirebaseToken
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.repository.ProductRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class LoginViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    @Inject
    private val loginRepository = LoginRepository(RetrofitFactory.buildService(Api::class.java))

    fun createUser(account: GoogleSignInAccount): User {
        return User(email = account.email!!,
            givenName = account.givenName!!,
            familyName = account.familyName!!,
            photo = account.photoUrl.toString().split('=').get(0),
            clientId = null,
            username = null,
            phone = null,
            token = ""
        )
    }

    fun sendFirebaseToken(token:String,firebase:FirebaseToken){
        loginRepository.sendToken(token, firebase)
    }
}