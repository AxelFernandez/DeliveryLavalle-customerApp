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

    fun deleteAddress(address: Address){
        addressRepository.deleteAddress(address)
    }

    fun notifyCorrectDeleted():LiveData<String>{
        return addressRepository.addressDeleted
    }

    fun buildAddress(v:View,address: Address? = null): Address {

        if (address != null) {
            return address.copy(
                street = v.street.text.toString(), number = v.number.text.toString(),
                district = v.district.text.toString(),
                floor = v.floor.text.toString(),
                reference = v.reference.text.toString()
            )
        }else{
            return Address(
                street = v.street.text.toString(), number = v.number.text.toString(),
                district = v.district.text.toString(),
                floor = v.floor.text.toString(),
                reference = v.reference.text.toString(),
                location = null
            )
        }
    }
    fun fillAddressView(address: Address, v :View){
        v.street.setText(address.street)
        v.number.setText(address.number)
        v.district.setText(address.district)
        v.floor.setText(address.floor)
        v.reference.setText(address.reference)

    }
}
