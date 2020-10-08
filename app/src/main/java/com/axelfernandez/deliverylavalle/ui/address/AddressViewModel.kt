package com.axelfernandez.deliverylavalle.ui.address

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.AddressResponse
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.repository.AddressRepository
import com.axelfernandez.deliverylavalle.repository.ClientRepository
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.ui.cellphone.CellPhoneViewModel
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import kotlinx.android.synthetic.main.address_fragment.view.*
import javax.inject.Inject

class AddressViewModel : ViewModel() {

    @Inject
    val login = LoginRepository(RetrofitFactory.buildService(Api::class.java))
    @Inject
    val addressRepository = AddressRepository(RetrofitFactory.buildService(Api::class.java))



    fun init(context: Context) {
        login.getToken(LoginUtils.getUserFromSharedPreferences(context)) //Start API Call and Update object
    }

    fun returnToken(): LiveData<UserResponse> {
        return login.returnData() //return token
    }

    fun soliciteAddress(token:String) {
         addressRepository.getAllAddress(token)
    }

    fun notifyAddres():LiveData<List<Address>>{
        return addressRepository.notifyAddress()
    }

    fun notifyPost():LiveData<AddressResponse>{
        return addressRepository.notifyPost()
    }

    fun postAddress(address: Address){
        var token = returnToken().value!!
        addressRepository.postAddress(address = address, token = token.access_token)
    }

    fun deleteAddress(address: Address, token: String){
        addressRepository.deleteAddress(token, address)
    }

    fun notifyCorrectDeleted():LiveData<String>{
        return addressRepository.addressDeleted
    }


}
