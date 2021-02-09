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



    lateinit var productRepository :ProductRepository
    lateinit var companyRepository : CompanyRepository

    fun getRepository(context: Context){
        productRepository  = ProductRepository(RetrofitFactory.buildService(Api::class.java, context))
        companyRepository  = CompanyRepository(RetrofitFactory.buildService(Api::class.java, context))
    }

    fun getProductCategoryByCompanyId(companyId: String){
        productRepository.getProductCategoryByCompany(companyId)
    }

    fun returnProductsCategory(): LiveData<List<ProductCategory>> {
        return productRepository.returnProductCategory()
    }

    fun getProductByCompanyId(productRequest: ProductRequest){
        productRepository.getProduct(productRequest)
    }

    fun returnProducts(): LiveData<List<Product>> {
        return productRepository.returnData()
    }

    fun getCompanyById(id :String){
        companyRepository.getCompanyById(id)
    }

    fun returnCompany(): LiveData<Company> {
        return companyRepository.returnCompanyDetail()
    }





}