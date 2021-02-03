package com.axelfernandez.deliverylavalle.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.axelfernandez.deliverylavalle.HomeActivity
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.FirebaseToken
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.login_fragment.view.*

class Login : Fragment() {
	val RC_SIGN_IN: Int = 1
	lateinit var mGoogleSignInClient: GoogleSignInClient
	lateinit var mGoogleSignInOptions: GoogleSignInOptions

	companion object {
		fun newInstance() = Login()
	}

	private lateinit var viewModel: LoginViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val v = inflater.inflate(R.layout.login_fragment, container, false)
		mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.token_client_id))
			.requestProfile()
			.requestEmail()
			.build()
		mGoogleSignInClient = GoogleSignIn.getClient(activity?.application!!, mGoogleSignInOptions)
		v.app_bar_1.text = "Delivery "
		v.app_bar_2.text = "Lavalle"
		v.google_button_sigin.setOnClickListener {
			signIn()
		}
		v.terms_and_conditions.movementMethod = LinkMovementMethod.getInstance();

		return v
	}

	private fun signIn() {
		val signInIntent: Intent = mGoogleSignInClient.signInIntent
		startActivityForResult(signInIntent, RC_SIGN_IN)
		view?.login_progress_bar?.visibility = View.VISIBLE

	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == RC_SIGN_IN) {
			val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
			try {
				val account = task.getResult(ApiException::class.java)!!
				val googleToken = account.idToken ?: return
				viewModel.loginWithGoogle(googleToken)
				viewModel.returnToken().observe(viewLifecycleOwner, Observer {
					LoginUtils.putUserToSharedPreferences(requireContext(), it.user)
					LoginUtils.saveSessionToken(it.access_token,requireContext())
					fetchFirebase()
					var bundle = bundleOf("clientId" to it.clientId)
					if (it.is_new || it.completeRegistry) {
						requireView().findNavController()
							.navigate(R.id.action_login_to_cellPhoneFragment, bundle)
					} else {
						LoginUtils.saveLoginReady(requireContext())
						val intent = Intent(context, HomeActivity::class.java)
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
						startActivity(intent)
						activity?.finish()
					}

				})


			} catch (e: ApiException) {
				ViewUtil.setSnackBar(requireView(), R.color.orange, e.message ?: return)
			}
		}

	}

	private fun fetchFirebase() {
		FirebaseMessaging.getInstance().token.addOnCompleteListener(
			OnCompleteListener { task ->
				if (!task.isSuccessful) {
					Log.w(
						"TAG",
						"Fetching FCM registration token failed",
						task.exception
					)
					return@OnCompleteListener
				}
				val token = task.result
				LoginUtils.saveTokenFirebase(token, requireContext())
				val user = LoginUtils.getUserFromSharedPreferences(requireContext())
				viewModel.sendFirebaseToken(user.token, FirebaseToken(token))
			})
	}


}