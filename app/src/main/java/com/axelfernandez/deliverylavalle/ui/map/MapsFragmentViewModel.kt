package com.axelfernandez.deliverylavalle.ui.map

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.AddressResponse
import com.axelfernandez.deliverylavalle.repository.AddressRepository

class MapsFragmentViewModel : ViewModel(){


	lateinit var addressRepository : AddressRepository


	fun getRepository(context: Context) {
		addressRepository  = AddressRepository(RetrofitFactory.buildService(Api::class.java,context))
	}

	fun notifyPost(): LiveData<AddressResponse> {
		return addressRepository.notifyPost()
	}

	fun postAddress(address: Address){
		addressRepository.postAddress(address = address)
	}

}