package com.axelfernandez.deliverylavalle.ui.login

import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.models.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class LoginViewModel : ViewModel() {
    // TODO: Implement the ViewModel


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

}