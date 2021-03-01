package com.axelfernandez.deliverylavalle.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.ConfirmationObject
import com.axelfernandez.deliverylavalle.models.FirebaseToken
import com.axelfernandez.deliverylavalle.repository.LoginRepository

class ProfileViewModel : ViewModel() {

	lateinit var loginRepository : LoginRepository

	fun getRepository(context: Context){
		loginRepository =  LoginRepository(RetrofitFactory.buildService(Api::class.java,context))
	}


	fun deleteFirebaseToken(firebase: FirebaseToken){
		loginRepository.deleteToken(firebase)
	}
	fun returnLogout(): LiveData<ConfirmationObject> {
		return loginRepository.returnFirebaseResponse()
	}
}