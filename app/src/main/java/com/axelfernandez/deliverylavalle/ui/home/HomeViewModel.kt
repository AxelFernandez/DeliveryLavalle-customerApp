package com.axelfernandez.deliverylavalle.ui.home

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.R
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
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlin.math.log

class HomeViewModel : ViewModel() {

    lateinit var fusedLocationClient :FusedLocationProviderClient
    lateinit var companyCategoryResponse :CompanyRepository

    lateinit var resultLocation : Location
    lateinit var locationRequest : LocationRequest

    fun getRepository(context: Context){
        companyCategoryResponse = CompanyRepository(RetrofitFactory.buildService(Api::class.java,context))
    }


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



    fun getCategoty(){
        companyCategoryResponse.getCategory()

    }
    fun returnCategory(): LiveData<List<CompanyCategoryResponse>> {
        return companyCategoryResponse.returnData()

    }


    fun getCompany(location: Location){
        companyCategoryResponse.getCompany(location)

    }
    fun returnCompany(): LiveData<List<Company>> {
        return companyCategoryResponse.returnCompany()

    }


    fun getLocationAndGetCompany(activity: Activity, category : String?,v : View){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null){
                startLocationUpdates(activity)
            }else{
                resultLocation = Location(location.latitude.toString(),location.longitude.toString(), category)
                Log.e("LOCATION", "Get location successful!")
                getCompany(resultLocation)
            }


        }.addOnFailureListener {
            Log.e("LOCATION", it.message?:"Error en Home View Model Ln 87")
            ViewUtil.checkPermission(context = activity)
            ViewUtil.setSnackBar(v, R.color.orange,"No hay Permisos para acceder a la Ubicacion, revisalos!")
            return@addOnFailureListener
        }


    }
    fun createLocationRequest() {
         locationRequest = LocationRequest.create()?.apply {
             interval = 10000
             fastestInterval = 5000
             priority = LocationRequest.PRIORITY_HIGH_ACCURACY
         }?:return
    }
    private fun startLocationUpdates(activity: Activity) {
        ViewUtil.checkPermission(activity)
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations){
                resultLocation = Location(location.latitude.toString(),location.longitude.toString(), null)
                Log.e("LOCATION", "Get location successful!")
                getCompany(resultLocation)
                fusedLocationClient.removeLocationUpdates(this)

            }
        }
    }

}
