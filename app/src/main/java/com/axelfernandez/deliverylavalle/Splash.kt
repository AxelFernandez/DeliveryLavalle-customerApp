package com.axelfernandez.deliverylavalle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.repository.LoginRepository
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class Splash : AppCompatActivity() {
	private lateinit var fullscreenContent: TextView
	private lateinit var fullscreenContentControls: LinearLayout
	private val hideHandler = Handler()
	lateinit var login: LoginRepository

	@SuppressLint("InlinedApi")
	private val hidePart2Runnable = Runnable {
		// Delayed removal of status and navigation bar

		// Note that some of these constants are new as of API 16 (Jelly Bean)
		// and API 19 (KitKat). It is safe to use them, as they are inlined
		// at compile-time and do nothing on earlier devices.
		fullscreenContent.systemUiVisibility =
			View.SYSTEM_UI_FLAG_LOW_PROFILE or
					View.SYSTEM_UI_FLAG_FULLSCREEN or
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	}
	private val showPart2Runnable = Runnable {
		// Delayed display of UI elements
		supportActionBar?.show()
		fullscreenContentControls.visibility = View.VISIBLE
	}
	private var isFullscreen: Boolean = false

	private val hideRunnable = Runnable { hide() }


	@SuppressLint("ClickableViewAccessibility")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_splash)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		isFullscreen = true

		// Set up the user interaction to manually show or hide the system UI.
		fullscreenContent = findViewById(R.id.fullscreen_content)
		fullscreenContent.setOnClickListener { toggle() }

		fullscreenContentControls = findViewById(R.id.fullscreen_content_controls)


	}

	override fun onResume() {
		super.onResume()
		startLogin()
	}

	override fun onPostCreate(savedInstanceState: Bundle?) {
		super.onPostCreate(savedInstanceState)

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100)
	}

	private fun startLogin() {
		FirebaseCrashlytics.getInstance().setCustomKey("environment", BuildConfig.BUILD_TYPE)
		var editor = getSharedPreferences("userSession", Context.MODE_PRIVATE)
		val is_login_ready = editor.getBoolean(getString(R.string.is_login_ready), false)
		var remoteConfig = Firebase.remoteConfig
		login = LoginRepository(RetrofitFactory.buildService(Api::class.java, baseContext))
		val configSettings = remoteConfigSettings {
			minimumFetchIntervalInSeconds = 3600
		}
		remoteConfig.setConfigSettingsAsync(configSettings)
		remoteConfig.fetchAndActivate()
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					val updated = task.result
					FirebaseCrashlytics.getInstance().log("Config params updated: $updated")
				} else {
					FirebaseCrashlytics.getInstance().log("Can't Connect to Firebase")
				}
				if (is_login_ready) {
					val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
					if (googleSignInAccount?.idToken != null) {
						login.getToken(googleSignInAccount.idToken?:return@addOnCompleteListener)
					}else{
						val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
							.requestIdToken(BuildConfig.tokenGoogleClient)
							.requestEmail()
							.requestProfile()
							.build()
						val googleSignInClient = GoogleSignIn.getClient(this, gso)
						googleSignInClient.silentSignIn().addOnCompleteListener {
							val account: GoogleSignInAccount? = it.result
							login.getToken(account?.idToken?:return@addOnCompleteListener)
						}
					}

					login.returnData().observe(this, Observer {
                        if (it == null) {
	                        val intent = Intent(this, MainActivity::class.java)
	                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
	                        startActivity(intent)
	                        finish()
                        }
                        val it = it ?: return@Observer
                        val user: User = LoginUtils.getUserFromSharedPreferences(this)
                        user.token = it.access_token
                        LoginUtils.putUserToSharedPreferences(this, user)
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    })
				} else {
					val intent = Intent(this, MainActivity::class.java)
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
					startActivity(intent)
					finish()
				}
			}
	}

	private fun toggle() {
		if (isFullscreen) {
			hide()
		} else {
			show()
		}
	}

	private fun hide() {
		// Hide UI first
		supportActionBar?.hide()
		fullscreenContentControls.visibility = View.GONE
		isFullscreen = false

		// Schedule a runnable to remove the status and navigation bar after a delay
		hideHandler.removeCallbacks(showPart2Runnable)
		hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
	}

	private fun show() {
		// Show the system bar
		fullscreenContent.systemUiVisibility =
			View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		isFullscreen = true

		// Schedule a runnable to display UI elements after a delay
		hideHandler.removeCallbacks(hidePart2Runnable)
		hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
	}

	/**
	 * Schedules a call to hide() in [delayMillis], canceling any
	 * previously scheduled calls.
	 */
	private fun delayedHide(delayMillis: Int) {
		hideHandler.removeCallbacks(hideRunnable)
		hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
	}

	companion object {
		/**
		 * Whether or not the system UI should be auto-hidden after
		 * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
		 */
		private const val AUTO_HIDE = true

		/**
		 * If [AUTO_HIDE] is set, the number of milliseconds to wait after
		 * user interaction before hiding the system UI.
		 */
		private const val AUTO_HIDE_DELAY_MILLIS = 3000

		/**
		 * Some older devices needs a small delay between UI widget updates
		 * and a change of the status and navigation bar.
		 */
		private const val UI_ANIMATION_DELAY = 300
	}
}