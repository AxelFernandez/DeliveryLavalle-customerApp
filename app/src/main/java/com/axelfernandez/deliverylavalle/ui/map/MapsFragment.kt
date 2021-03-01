package com.axelfernandez.deliverylavalle.ui.map

import android.content.ContentValues.TAG
import android.content.Intent
import android.location.Location
import android.os.Bundle
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
import com.axelfernandez.deliverylavalle.ui.address.AddressViewModel
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.app_bar.view.*


class MapsFragment : Fragment() {
	lateinit var marker: MarkerOptions
	lateinit var address: Address
	private lateinit var viewModel: MapsFragmentViewModel

	private val callback = OnMapReadyCallback { googleMap ->
		/**
		 * Manipulates the map once available.
		 * This callback is triggered when the map is ready to be used.
		 * This is where we can add markers or lines, add listeners or move the camera.
		 * In this case, we just add a marker near Sydney, Australia.
		 * If Google Play services is not installed on the device, the user will be prompted to
		 * install it inside the SupportMapFragment. This method will only be triggered once the
		 * user has installed Google Play services and returned to the app.
		 */
		val arguments = arguments ?: return@OnMapReadyCallback
		val location =
			arguments.getParcelable<Location>(resources.getString(R.string.argument_location))
				?: return@OnMapReadyCallback

		var position = LatLng(location.latitude, location.longitude)
		if (address.location != null) {
			val positionSplited = address.location?.split(",") ?: return@OnMapReadyCallback
			position = LatLng(positionSplited[0].toDouble(), positionSplited[1].toDouble())
		}

		marker = MarkerOptions().draggable(true).position(position).title("Marker in Sydney")
		googleMap.addMarker(marker)
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
		googleMap.moveCamera(CameraUpdateFactory.zoomTo(18F))

		googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
			override fun onMarkerDragStart(arg0: Marker) {

			}

			override fun onMarkerDragEnd(arg0: Marker) {
				googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))

			}

			override fun onMarkerDrag(arg0: Marker?) {
				address.location = formatMarker(arg0?.position?:return)

			}
		})

	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_maps, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = ViewModelProviders.of(this).get(MapsFragmentViewModel::class.java)
		viewModel.getRepository(requireContext())

		val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
		val arguments = arguments ?: return
		val mustReturn = arguments.getBoolean(getString(R.string.argument_must_return), false)
		address = arguments.getParcelable(resources.getString(R.string.arguments_address)) ?: return
		val toolbar = view.findViewById(R.id.toolbar) as Toolbar
		toolbar.setNavigationIcon(R.drawable.ic_back_button)
		toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
		toolbar.inflateMenu(R.menu.check_menu)
		toolbar.setOnMenuItemClickListener {
			if (it.itemId == R.id.check_icon) {
				viewModel.postAddress(address)
			}
			return@setOnMenuItemClickListener true
		}
		mapFragment?.getMapAsync(callback)


		viewModel.notifyPost().observe(viewLifecycleOwner, Observer {
			if (it != null) {
				LoginUtils.saveLoginReady(requireContext())
				ViewUtil.setSnackBar(view, R.color.orange, "Direccion Guardada Correctamente")
				address.id = it.addressId
				LoginUtils.saveDefaultAddress(requireContext(), address)
				if (mustReturn) {
					view.findNavController().navigate(R.id.action_mapsFragment_to_addressFragment3)
				} else {
					val intent = Intent(context, HomeActivity::class.java)
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
					startActivity(intent)
					activity?.finish()
				}
			} else {
				ViewUtil.setSnackBar(
					view,
					R.color.red,
					"Ops! Hubo un problema, intenta luego nuevamente"
				)
			}

		})

	}

	private fun formatMarker(location: LatLng): String {
		return "%s,%s".format(location.latitude, location.longitude)
	}

}