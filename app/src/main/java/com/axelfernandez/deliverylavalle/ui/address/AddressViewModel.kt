package com.axelfernandez.deliverylavalle.ui.address

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.*
import com.axelfernandez.deliverylavalle.repository.AddressRepository
import com.axelfernandez.deliverylavalle.repository.ClientRepository
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.ui.cellphone.CellPhoneViewModel
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.address_fragment.view.*
import javax.inject.Inject

class AddressViewModel : ViewModel() {


    lateinit var addressRepository :AddressRepository


    fun getRepository(context: Context) {
        addressRepository  = AddressRepository(RetrofitFactory.buildService(Api::class.java,context))
    }

    fun soliciteAddress() {
         addressRepository.getAllAddress()
    }

    fun notifyAddres():LiveData<List<Address>>{
        return addressRepository.notifyAddress()
    }

    fun notifyPost():LiveData<AddressResponse>{
        return addressRepository.notifyPost()
    }

    fun postAddress(address: Address){
        addressRepository.postAddress(address = address)
    }

    fun deleteAddress(address: Address){
        addressRepository.deleteAddress(address)
    }

    fun notifyCorrectDeleted():LiveData<String>{
        return addressRepository.addressDeleted
    }


}
