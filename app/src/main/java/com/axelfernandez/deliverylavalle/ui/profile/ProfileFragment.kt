package com.axelfernandez.deliverylavalle.ui.profile

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.axelfernandez.deliverylavalle.BuildConfig
import com.axelfernandez.deliverylavalle.MainActivity
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.FirebaseToken
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

	private lateinit var profileViewModel: ProfileViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
		profileViewModel.getRepository(requireContext())
		val root = inflater.inflate(R.layout.fragment_profile, container, false)
		val user = LoginUtils.getUserFromSharedPreferences(requireContext())

		Picasso.with(requireContext()).load(user.photo).error(R.drawable.no_profile).into(root.settings_image)

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
			val mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(BuildConfig.tokenGoogleClient)
				.requestProfile()
				.requestEmail()
				.build()
			val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity().application, mGoogleSignInOptions)
			mGoogleSignInClient.signOut().addOnSuccessListener {
				profileViewModel.deleteFirebaseToken(FirebaseToken(LoginUtils.getTokenFirebase(requireContext())?:return@addOnSuccessListener))
			}.addOnFailureListener {
				ViewUtil.setSnackBar(requireView(),R.color.orange,"Hubo un problema para desloguear, vuelve a intentarlo")
				FirebaseCrashlytics.getInstance().recordException(it)
			}

			profileViewModel.returnLogout().observe(viewLifecycleOwner, Observer {
				if (it ==null){
					ViewUtil.setSnackBar(requireView(),R.color.orange,"Hubo un problema para desloguear, vuelve a intentarlo")
					return@Observer
				}
				LoginUtils.removeUserData(requireContext())
				val intent = Intent(context, MainActivity::class.java)
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
				startActivity(intent)
				activity?.finish()
			})

		}
		root.settings_terms_and_conditions.setOnClickListener {
			val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://deliverylavalle.com.ar/terminosycondiciones/"))
			startActivity(browserIntent)

		}

		return root
	}
}