package com.axelfernandez.deliverylavalle.ui.address

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.axelfernandez.deliverylavalle.HomeActivity
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.address_fragment.view.*
import kotlinx.android.synthetic.main.app_bar.view.*


class AddressFragment : Fragment() {

    companion object {
        fun newInstance() = AddressFragment()
    }

    private lateinit var v: View
    private lateinit var viewModel: AddressViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var address: Address
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
        val mustReturn = arguments?.getBoolean(getString(R.string.argument_must_return),false)?:false
        v.app_bar_1.text = "Agregar "
        v.app_bar_2.text = "Dirección"
        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        if (mustReturn){
            toolbar.inflateMenu(R.menu.trash_menu)
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.trash_icon){
                    it.isCheckable

                }
                return@setOnMenuItemClickListener true
            }
        }
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
                    ViewUtil.setSnackBar(v,R.color.orange,"Localizacion recuperada correctamente")
                    v.get_location.setImageResource(R.drawable.ic_baseline_check_circle_24)
                }

            }.addOnFailureListener {
                Log.e("LOCATION", it.message!!)
                ViewUtil.checkPermission(context = requireActivity());
            }
        })
        viewModel.notifyPost().observe(viewLifecycleOwner, Observer {
            if(it != null){
                var editor = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)?.edit()
                editor?.putBoolean(getString(R.string.is_login_ready),true)
                editor?.apply()
                ViewUtil.setSnackBar(v,R.color.orange,"Direccion Guardada Correctamente")
                address.id = it.addressId
                LoginUtils.saveDefaultAddress(requireContext(),address)
                if(mustReturn){
                    v.findNavController().popBackStack()
                }else{
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    activity?.finish()
                }
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

    private fun continuePost() {
        address = Address(
            street = v.street.text.toString(), number = v.number.text.toString(),
            district = v.district.text.toString(),
            floor = v.floor.text.toString(),
            reference = v.reference.text.toString(),
            location = phoneLocation,
            id = null
        )
        val user = LoginUtils.getUserFromSharedPreferences(requireContext())
        viewModel.postAddress(address,user.token)
    }

}



