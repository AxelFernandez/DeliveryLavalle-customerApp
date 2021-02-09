package com.axelfernandez.deliverylavalle.api

import android.content.Context
import com.axelfernandez.deliverylavalle.BuildConfig
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitFactory {

    private fun getRetrofit(context: Context):Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.hostname + "/api/") // change this IP for testing in Gradle files
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient(context))
            .build()
    }
    private fun getClient(context: Context): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor {chain->
            val requestBuilder = chain.request().newBuilder()
            if (!chain.request().url().encodedPath().contains("google")) {
                requestBuilder.addHeader("Authorization", "Bearer %s".format(LoginUtils.getUserFromSharedPreferences(context).token))
            }
            chain.proceed(requestBuilder.build())
        }
        return client.build()



    }
    fun<T> buildService(service: Class<T>, context: Context): T{
        return getRetrofit(context).create(service)
    }
}