package com.axelfernandez.deliverylavalle.ui.companyDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.*
import com.axelfernandez.deliverylavalle.repository.CompanyRepository
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.repository.ProductRepository
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import javax.inject.Inject


class DetailViewModel : ViewModel() {


    @Inject
    private val productRepository = ProductRepository(RetrofitFactory.buildService(Api::class.java))
    @Inject
    private val companyRepository = CompanyRepository(RetrofitFactory.buildService(Api::class.java))





    fun getProductCategoryByCompanyId(token:String, companyId: String){
        productRepository.getProductCategoryByCompany(token, companyId)
    }

    fun returnProductsCategory(): LiveData<List<ProductCategory>> {
        return productRepository.returnProductCategory()
    }

    fun getProductByCompanyId(token:String, productRequest: ProductRequest){
        productRepository.getProduct(token, productRequest)
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