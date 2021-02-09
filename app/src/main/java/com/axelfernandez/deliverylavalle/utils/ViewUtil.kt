package com.axelfernandez.deliverylavalle.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class ViewUtil {

	companion object {
		fun setSnackBar(v: View, color: Int, text: String) {
			var snackbar: Snackbar = Snackbar.make(
				v, text,
				Snackbar.LENGTH_LONG
			)
			snackbar.view.setBackgroundColor(ContextCompat.getColor(v.context, color))
			snackbar.show()
		}

		fun checkPermission(context: Activity) {
			checkIfGPSIsEnabled(context)
			if (ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.ACCESS_FINE_LOCATION
				) != PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.ACCESS_COARSE_LOCATION
				) != PackageManager.PERMISSION_GRANTED
			) { //Can add more as per requirement
				ActivityCompat.requestPermissions(
					context,
					arrayOf(
						Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.ACCESS_COARSE_LOCATION
					),
					123
				)
			}
		}

		fun checkIfGPSIsEnabled(activity: Activity) {
			val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
			if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				checkGPSEnable(activity)
			}


		}

		private fun checkGPSEnable(context: Activity) {
			val dialogBuilder = AlertDialog.Builder(context)
			dialogBuilder.setMessage("Delivery Lavalle necesita tu ubicación para buscar emprendedores cerca, por favor actívalo para continuar")
				.setCancelable(false)
				.setPositiveButton("Activar", DialogInterface.OnClickListener { dialog, id
					->
					ContextCompat.startActivity(
						context,
						Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
						null
					)
				})
			val alert = dialogBuilder.create()
			alert.show()
		}


	}
}