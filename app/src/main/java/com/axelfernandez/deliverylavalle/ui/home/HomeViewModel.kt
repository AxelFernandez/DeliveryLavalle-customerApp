package com.axelfernandez.deliverylavalle.ui.home

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.CompanyCategoryResponse
import com.axelfernandez.deliverylavalle.models.Location
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.repository.ClientRepository
import com.axelfernandez.deliverylavalle.repository.CompanyRepository
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import kotlin.math.log

class HomeViewModel : ViewModel() {

    @Inject
    val login = LoginRepository(RetrofitFactory.buildService(Api::class.java))
    @Inject
    val companyCategoryResponse = CompanyRepository(RetrofitFactory.buildService(Api::class.java))



    private val banner_title = MutableLiveData<String>().apply {
        value = Firebase.remoteConfig.getString("banner_title")
    }
    val banner_title_vm: LiveData<String> = banner_title

    private val banner_subtitle = MutableLiveData<String>().apply {
        value = Firebase.remoteConfig.getString("banner_subtitle")
    }
    val banner_subtitle_vm: LiveData<String> = banner_subtitle

    private val banner_image = MutableLiveData<String>().apply {
        value = Firebase.remoteConfig.getString("banner_image_url")
    }
    val banner_image_vm: LiveData<String> = banner_image

    fun initial(context: Context){
        login.getToken(LoginUtils.getUserFromSharedPreferences(context = context))
    }
    fun returnToken(): LiveData<UserResponse> {
        return login.returnData()
    }

    fun getCategoty(token:String){
        companyCategoryResponse.getCategory(token)

    }
    fun returnCategory(): LiveData<List<CompanyCategoryResponse>> {
        return companyCategoryResponse.returnData()

    }


    fun getCompany(token:String,location: Location){
        companyCategoryResponse.getCompany(token,location)

    }
    fun returnCompany(): LiveData<List<Company>> {
        return companyCategoryResponse.returnCompany()

    }

    fun getLocationAndGetCompany(activity: Activity,token: String, category : String?){
        var fusedLocationClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            var location = Location(location.latitude.toString(),location.longitude.toString(), category)
            Log.e("LOCATION", "Get location successful!")
            getCompany(token,location)

        }.addOnFailureListener {
            Log.e("LOCATION", it.message!!)
            ViewUtil.checkPermission(context = activity);
        }

    }


}
