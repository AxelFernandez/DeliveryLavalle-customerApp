package com.axelfernandez.deliverylavalle.ui.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.axelfernandez.deliverylavalle.HomeActivity
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.Location
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.address_fragment.view.*
import kotlinx.android.synthetic.main.app_bar.view.*


class AddressFragment : Fragment() {

	companion object {
		fun newInstance() = AddressFragment()
	}

	private lateinit var v: View
	private lateinit var viewModel: AddressViewModel
	private lateinit var fusedLocationClient: FusedLocationProviderClient
	private var address: Address? = null
	lateinit var locationRequest: LocationRequest


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
		viewModel.getRepository(requireContext())
		createLocationRequest()
		val arguments = arguments ?: return
		val isOnBoarding = arguments.getBoolean(getString(R.string.in_onboarding), false)
		val mustReturn = arguments.getBoolean(getString(R.string.argument_must_return), false)
		val addressToFill = arguments.getParcelable<Address>(getString(R.string.arguments_address))
		if (addressToFill != null) {
			address = addressToFill
		}
		addressToFill?.let {
			viewModel.fillAddressView(it, v)
		}
		v.app_bar_1.text = "Agregar "
		v.app_bar_2.text = "Dirección"
		val toolbar = v.findViewById(R.id.toolbar) as Toolbar
		if (mustReturn) {
			val toolbar = v.findViewById(R.id.toolbar) as Toolbar
			toolbar.setNavigationIcon(R.drawable.ic_back_button)
			toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
		}
		if (isOnBoarding) {
			toolbar.inflateMenu(R.menu.skip_menu)
			toolbar.setOnMenuItemClickListener {
				if (it.itemId == R.id.item_skip_menu) {
					LoginUtils.saveLoginReady(requireContext()) //Warning! Possible Bug
					val intent = Intent(context, HomeActivity::class.java)
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
					startActivity(intent)
					activity?.finish()
				}
				return@setOnMenuItemClickListener true
			}
		}

		fusedLocationClient = LocationServices.getFusedLocationProviderClient(v.context)



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

		address = viewModel.buildAddress(v,address)

		fusedLocationClient = LocationServices.getFusedLocationProviderClient(v.context)
		fusedLocationClient.lastLocation.addOnSuccessListener { location ->
			if (location == null) {
				startLocationUpdates(requireActivity())

			} else {
				ViewUtil.setSnackBar(v, R.color.orange, "Localizacion recuperada correctamente")
				val mapBundle = arguments?:return@addOnSuccessListener
				mapBundle.putParcelable(getString(R.string.arguments_address), address)
				mapBundle.putParcelable(getString(R.string.argument_location), location)
				v.findNavController()
					.navigate(R.id.action_addressFragment2_to_mapsFragment, mapBundle)
			}

		}.addOnFailureListener {
			Log.e("LOCATION", it.message ?: "Error in Line 95 Address Fragment")
			ViewUtil.checkPermission(context = requireActivity());
			ViewUtil.setSnackBar(
				v,
				R.color.orange,
				"Necesitamos tu ubicacion para poder ofrecerte emprendedores cerca"
			)
		}
	}


	fun createLocationRequest() {
		locationRequest = LocationRequest.create()?.apply {
			interval = 10000
			fastestInterval = 5000
			priority = LocationRequest.PRIORITY_HIGH_ACCURACY
		} ?: return
	}

	private fun startLocationUpdates(activity: Activity) {
		ViewUtil.checkPermission(activity)
		fusedLocationClient.requestLocationUpdates(
			locationRequest,
			locationCallback,
			Looper.getMainLooper()
		)
	}

	private val locationCallback = object : LocationCallback() {
		override fun onLocationResult(locationResult: LocationResult?) {
			locationResult ?: return
			for (location in locationResult.locations) {
				Log.e("LOCATION", "Get location successful!")
				address?.location = "%s,%s".format(location.latitude, location.longitude)
				ViewUtil.setSnackBar(v, R.color.orange, "Localizacion recuperada correctamente")
				fusedLocationClient.removeLocationUpdates(this)
			}
		}
	}
}



