package com.axelfernandez.deliverylavalle.ui.profile

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.axelfernandez.deliverylavalle.MainActivity
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

	private lateinit var profileViewModel: ProfileViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
		val root = inflater.inflate(R.layout.fragment_profile, container, false)
		val user = LoginUtils.getUserFromSharedPreferences(requireContext())

		Picasso.with(requireContext()).load(user.photo).into(root.settings_image, object: com.squareup.picasso.Callback {
			override fun onSuccess() {
				val imageBitmap =
					(root.settings_image.getDrawable() as BitmapDrawable).bitmap
				val imageDrawable =
					RoundedBitmapDrawableFactory.create(resources, imageBitmap)
				imageDrawable.isCircular = true
				imageDrawable.cornerRadius = Math.max(
					imageBitmap.width,
					imageBitmap.height
				) / 2.0f
				root.settings_image.setImageDrawable(imageDrawable)
			}

			override fun onError() {
				TODO("Not yet implemented")
			}

		})

		root.settings_name.text = user.givenName + user.familyName
		root.settings_email.text = user.email
		val bundle = Bundle()
		bundle.putBoolean(getString(R.string.from_settings),true)
		root.settings_address.setOnClickListener {
			findNavController().navigate(R.id.action_navigation_notifications_to_addressList2,bundle)
		}
		root.settings_update_phone.setOnClickListener {
			findNavController().navigate(R.id.action_navigation_notifications_to_cellPhoneFragment2,bundle)
		}
		root.settings_logout.setOnClickListener {
			//TODO: Refactor to use Navigation Component
			LoginUtils.removeUserData(requireContext())
			val intent = Intent(context, MainActivity::class.java)
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			activity?.finish()
		}


		return root
	}
}