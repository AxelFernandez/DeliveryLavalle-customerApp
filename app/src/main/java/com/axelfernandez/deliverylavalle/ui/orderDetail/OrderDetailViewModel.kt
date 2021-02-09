package com.axelfernandez.deliverylavalle.ui.orderDetail

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.Product
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.repository.CompanyRepository
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.repository.ProductRepository
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import javax.inject.Inject

class OrderDetailViewModel : ViewModel() {



    lateinit var companyRepository : CompanyRepository

    fun getRepository(context: Context){
        companyRepository = CompanyRepository(RetrofitFactory.buildService(Api::class.java,context))
    }

    private val _gropuped = MutableLiveData<Map<String,List<Product>>>()
    val gropuped: LiveData<Map<String,List<Product>>> = _gropuped


    fun initial(bundle: Bundle){
        var array: ArrayList<Product>? = bundle.get("orders") as ArrayList<Product>?
        _gropuped.value =  array?.groupBy { it.name }

    }


    fun getCompanyById(token:String){
        companyRepository.getCompanyById(token)
    }

    fun returnCompany(): LiveData<Company> {
        return companyRepository.returnCompanyDetail()
    }
}