package com.axelfernandez.deliverylavalle.ui.orderSelectPayment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.PaymentMethods
import com.axelfernandez.deliverylavalle.repository.CompanyRepository
import javax.inject.Inject

class OrderSelectPaymentAndAddressViewModel : ViewModel() {


    private lateinit var companyRepository : CompanyRepository

    fun getRepository(context: Context){
        companyRepository  = CompanyRepository(RetrofitFactory.buildService(Api::class.java, context))
    }

    fun solicitPaymentMethod(id :String){
        companyRepository.getPaymentMethodByCompanyId(id)
    }

    fun returnPaymentMethod(): LiveData<PaymentMethods> {
        return companyRepository.returnPaymentMethod()
    }

    fun solicitDeliveryMethod(id :String){
        companyRepository.getDeliveryMethodByCompanyId(id)
    }

    fun returnDeliveryMethod(): LiveData<PaymentMethods> {
        return companyRepository.returnDeliveryMethod()
    }

}