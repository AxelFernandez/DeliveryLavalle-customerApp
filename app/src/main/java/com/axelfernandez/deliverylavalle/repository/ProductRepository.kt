package com.axelfernandez.deliverylavalle.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.models.Product
import com.axelfernandez.deliverylavalle.models.ProductCategory
import com.axelfernandez.deliverylavalle.models.ProductRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(
    private val api : Api

){
    val data = MutableLiveData<List<Product>>()
    val productCategory = MutableLiveData<List<ProductCategory>>()


    fun getProduct(productRequest: ProductRequest): MutableLiveData<List<Product>> {
        api.getProductByCompanyId(productRequest).enqueue(object :
            Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                data.value = null
            }
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                data.value = response.body()

            }
        })
        return data

    }

    fun returnData(): LiveData<List<Product>> {
        return data
    }

    fun getProductCategoryByCompany(companyId: String): MutableLiveData<List<ProductCategory>> {
        api.getProductCategoriesByCompanyId(companyId).enqueue(object :
            Callback<List<ProductCategory>> {
            override fun onFailure(call: Call<List<ProductCategory>>, t: Throwable) {
                productCategory.value = null
            }
            override fun onResponse(call: Call<List<ProductCategory>>, response: Response<List<ProductCategory>>) {
                productCategory.value = response.body()
            }
        })
        return productCategory
    }

    fun returnProductCategory(): LiveData<List<ProductCategory>> {
        return productCategory
    }
}