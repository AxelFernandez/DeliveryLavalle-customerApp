package com.axelfernandez.deliverylavalle.ui.detail

import android.R
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andremion.counterfab.CounterFab
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.Product
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.repository.CompanyRepository
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.repository.ProductRepository
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import javax.inject.Inject


class DetailViewModel : ViewModel() {

    @Inject
    private val login = LoginRepository(RetrofitFactory.buildService(Api::class.java))
    @Inject
    private val productRepository = ProductRepository(RetrofitFactory.buildService(Api::class.java))
    @Inject
    private val companyRepository = CompanyRepository(RetrofitFactory.buildService(Api::class.java))



    fun initial(context: Context){
        login.getToken(LoginUtils.getUserFromSharedPreferences(context = context))
    }
    fun returnToken(): LiveData<UserResponse> {
        return login.returnData()
    }

    fun getProductByCompanyId(token:String, id :String){
        productRepository.getProduct(token, id)
    }

    fun returnProducts(): LiveData<List<Product>> {
        return productRepository.returnData()
    }

    fun getCompanyById(token:String, id :String){
        companyRepository.getCompanyById(token, id)
    }

    fun returnCompany(): LiveData<Company> {
        return companyRepository.returnCompanyDetail()
    }





}