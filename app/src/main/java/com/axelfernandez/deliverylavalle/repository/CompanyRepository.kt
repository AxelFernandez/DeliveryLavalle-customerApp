package com.axelfernandez.deliverylavalle.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyRepository (
    private val api : Api

    ){
        val data = MutableLiveData<List<CompanyCategoryResponse>>()
        val company = MutableLiveData<List<Company>>()
        val companyDetail = MutableLiveData<Company>()
        val paymentMethods = MutableLiveData<PaymentMethods>()
        val deliveryMethods = MutableLiveData<PaymentMethods>()


        fun getCategory(): MutableLiveData<List<CompanyCategoryResponse>> {
            api.getCompanyCategories().enqueue(object : Callback<List<CompanyCategoryResponse>> {
                override fun onFailure(call: Call<List<CompanyCategoryResponse>>, t: Throwable) {
                    data.value = null
                }
                override fun onResponse(call: Call<List<CompanyCategoryResponse>>, response: Response<List<CompanyCategoryResponse>>) {
                    data.value = response.body()
                }
            })
            return data

        }

        fun returnData(): LiveData<List<CompanyCategoryResponse>> {
            return data
        }


    fun getCompany(location : Location): MutableLiveData<List<Company>>{
        api.getCompany(location).enqueue(object : Callback<List<Company>>{
            override fun onFailure(call: Call<List<Company>>, t: Throwable) {
                company.value = null
            }

            override fun onResponse(call: Call<List<Company>>, response: Response<List<Company>>) {
                company.value = response.body()
            }
        })
        return company
    }

    fun returnCompany(): MutableLiveData<List<Company>> {
        return company
    }


    fun getCompanyById(id : String): MutableLiveData<Company>{
        api.getCompanyById(id).enqueue(object : Callback<Company>{
            override fun onFailure(call: Call<Company>, t: Throwable) {
                companyDetail.value = null
            }

            override fun onResponse(call: Call<Company>, response: Response<Company>) {
                companyDetail.value = response.body()
            }
        })
        return companyDetail
    }

    fun returnCompanyDetail(): MutableLiveData<Company> {
        return companyDetail
    }


    fun getPaymentMethodByCompanyId(id : String): MutableLiveData<PaymentMethods>{
        api.getPaymentMethodByCompanyId(id).enqueue(object : Callback<PaymentMethods>{
            override fun onFailure(call: Call<PaymentMethods>, t: Throwable) {
                paymentMethods.value = null
            }

            override fun onResponse(call: Call<PaymentMethods>, response: Response<PaymentMethods>) {
                paymentMethods.value = response.body()
            }
        })
        return paymentMethods
    }

    fun returnPaymentMethod(): MutableLiveData<PaymentMethods> {
        return paymentMethods
    }

    fun getDeliveryMethodByCompanyId(id : String): MutableLiveData<PaymentMethods>{
        api.getDeliveryMethodByCompanyId(id).enqueue(object : Callback<PaymentMethods>{
            override fun onFailure(call: Call<PaymentMethods>, t: Throwable) {
                deliveryMethods.value = null
            }

            override fun onResponse(call: Call<PaymentMethods>, response: Response<PaymentMethods>) {
                deliveryMethods.value = response.body()
            }
        })
        return deliveryMethods
    }

    fun returnDeliveryMethod(): MutableLiveData<PaymentMethods> {
        return deliveryMethods
    }
}