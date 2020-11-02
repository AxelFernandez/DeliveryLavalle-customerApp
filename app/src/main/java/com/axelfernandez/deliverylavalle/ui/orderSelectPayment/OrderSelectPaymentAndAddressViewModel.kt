package com.axelfernandez.deliverylavalle.ui.orderSelectPayment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.PaymentMethods
import com.axelfernandez.deliverylavalle.repository.CompanyRepository
import javax.inject.Inject

class OrderSelectPaymentAndAddressViewModel : ViewModel() {

    @Inject
    private val companyRepository = CompanyRepository(RetrofitFactory.buildService(Api::class.java))

    fun solicitPaymentMethod(token:String, id :String){
        companyRepository.getPaymentMethodByCompanyId(token, id)
    }

    fun returnPaymentMethod(): LiveData<PaymentMethods> {
        return companyRepository.returnPaymentMethod()
    }

    fun solicitDeliveryMethod(token:String, id :String){
        companyRepository.getDeliveryMethodByCompanyId(token, id)
    }

    fun returnDeliveryMethod(): LiveData<PaymentMethods> {
        return companyRepository.returnDeliveryMethod()
    }

}