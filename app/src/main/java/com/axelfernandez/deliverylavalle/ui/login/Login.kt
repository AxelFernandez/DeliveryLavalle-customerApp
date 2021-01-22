package com.axelfernandez.deliverylavalle.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.ViewUtils
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.axelfernandez.deliverylavalle.*
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.FirebaseToken
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : Fragment() {
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    companion object {
        fun newInstance() = Login()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.login_fragment, container, false)
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity?.application!!, mGoogleSignInOptions)
        v.app_bar_1.text = "Delivery "
        v.app_bar_2.text = "Lavalle"
        v.google_button_sigin.setOnClickListener{
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
        // TODO: Use the ViewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val retrofit = RetrofitFactory.buildService(Api::class.java)
                //val intent = Intent(this, HomeActivity::class.java)
                retrofit.loginWithGoogle(viewModel.createUser(account))
                    .enqueue(object: Callback<UserResponse> {
                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            ViewUtil.setSnackBar(requireView(),R.color.red,"Hay problemas de conexion con el servidor, intenta nuevamente")
                            view?.login_progress_bar?.visibility = View.GONE
                        }
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>
                        ) {
                            val response : UserResponse? = response.body()

                            val editor = activity?.getSharedPreferences("userSession", Context.MODE_PRIVATE)?.edit()?:return
                            editor.putString(getString(R.string.email),account.email)
                            editor.putString(getString(R.string.given_name),account.givenName)
                            editor.putString(getString(R.string.family_name),account.familyName)
                            editor.putString(getString(R.string.client_id),response?.clientId)
                            editor.putString(getString(R.string.token),response?.access_token)
                            editor.putString(getString(R.string.picture),account.photoUrl.toString().split('=').get(0))
                            editor.apply()

                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                                        return@OnCompleteListener
                                    }
                                    val token = task.result
                                    editor.putString("token_firebase",token).apply()

                                    val user = LoginUtils.getUserFromSharedPreferences(requireContext())
                                    viewModel.sendFirebaseToken(user.token, FirebaseToken(token))

                                    Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
                            })
                            var bundle = bundleOf("clientId" to response?.clientId)
                            if(response?.is_new!! || response.completeRegistry){
                                view!!.findNavController().navigate(R.id.action_login_to_cellPhoneFragment,bundle)
                            }else{
                                editor?.putBoolean(getString(R.string.is_login_ready),true)
                                editor?.apply()
                                val intent = Intent(context, HomeActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                activity?.finish()
                            }
                        }

                    })


            } catch (e: ApiException) {

            }
        }

    }



}