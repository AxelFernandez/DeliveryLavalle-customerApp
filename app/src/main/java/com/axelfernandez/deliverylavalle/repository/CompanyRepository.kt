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


        fun getCategory(token :String): MutableLiveData<List<CompanyCategoryResponse>> {
            api.getCompanyCategories("Bearer %s".format(token)).enqueue(object : Callback<List<CompanyCategoryResponse>> {
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


    fun getCompany(token : String,location : Location): MutableLiveData<List<Company>>{
        api.getCompany("Bearer %s".format(token),location).enqueue(object : Callback<List<Company>>{
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


}