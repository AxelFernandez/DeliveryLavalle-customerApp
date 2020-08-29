package com.axelfernandez.deliverylavalle.ui.address

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.address_fragment.view.*
import kotlinx.android.synthetic.main.app_bar.view.*


class AddressFragment : Fragment() {

    companion object {
        fun newInstance() = AddressFragment()
    }

    private lateinit var v: View
    private lateinit var viewModel: AddressViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var phoneLocation: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.address_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        viewModel.init(v.context)
        v.app_bar_1.text = "Agregar "
        v.app_bar_2.text = "Dirección"
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(v.context!!)
        v.get_location.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    v.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    v.context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    phoneLocation = "%s,%s".format(location.latitude, location.longitude)
                    var snackbar: Snackbar = Snackbar.make(
                        v,
                        "Geolocalizacion recuperada correctamente",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.view.setBackgroundColor(
                        ContextCompat.getColor(
                            v.context,
                            R.color.orange
                        )
                    )
                    snackbar.view.setPadding(0, 0, 0, 30)
                    snackbar.show()
                    v.get_location.setImageResource(R.drawable.ic_baseline_check_circle_24)
                }

            }.addOnFailureListener {
                Log.e("LOCATION", it.message!!)
                var snackbar: Snackbar = Snackbar.make(
                    v,
                    "Habilita el permiso de Geolocalizacion para esto",
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(ContextCompat.getColor(v.context, R.color.red))
                snackbar.view.setPadding(0, 0, 0, 30)
                snackbar.show()
            }
        })
        viewModel.notifyPost().observe(viewLifecycleOwner, Observer {
            if(it != null){


                    var editor = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)?.edit()
                    editor?.putBoolean(getString(R.string.is_login_ready),true)
                    editor?.apply()
                    ViewUtil.setSnackBar(v,R.color.orange,"Direccion Guardada Correctamente")
                    //val intent = Intent(context, HomeActivity::class.java)
                    //startActivity(intent)

            }else{
                ViewUtil.setSnackBar(v,R.color.red,"Ops! Hubo un problema, intenta luego nuevamente")

            }

        })

        v.save_direction.setOnClickListener {
            when {
                v.street?.text.isNullOrEmpty() -> v.layout_street.error =
                    getString(R.string.required)
                v.number?.text.isNullOrEmpty() -> v.layout_number.error =
                    getString(R.string.required)
                v.district?.text.isNullOrEmpty() -> v.layout_district.error =
                    getString(R.string.required)
                else -> continuePost()
            }

        }



    }

    fun continuePost() {
        var address: Address = Address(
            street = v.street.text.toString(), number = v.number.text.toString(),
            district = v.district.text.toString(),
            floor = v.floor.text.toString(),
            reference = v.reference.text.toString(),
            location = phoneLocation)
        viewModel.postAddress(address)
    }

}



