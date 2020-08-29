package com.axelfernandez.deliverylavalle.ui.cellphone

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.models.UserResponse
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.cell_phone_fragment.view.*
import javax.inject.Inject


class CellPhoneFragment : Fragment() {

    companion object {
        fun newInstance() = CellPhoneFragment()
    }

    @Inject
    private lateinit var viewModel: CellPhoneViewModel
    private lateinit var v : View
    var must_redirect: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v =  inflater.inflate(R.layout.cell_phone_fragment, container, false)

        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CellPhoneViewModel::class.java)
        var user = viewModel.getUserData(v.context)
        v.app_bar_1.text = "Bienvenido "
        must_redirect = false
        var field :EditText = v.findViewById(R.id.cellphone_field)
        v.tilo_cellphone.helperText= getString(R.string.required)
        v.app_bar_2.text = "%s %s".format(user.givenName,user.familyName)
        v.phone_email.text = "Email: %s".format(user.email)
        v.phone_name.text = "Nombre: %s".format(user.givenName)
        v.phone_last_name.text = "Apellido: %s".format(user.familyName)
        Picasso.with(v.context)
            .load(user.photo)
            .into(v.phone_image, object: com.squareup.picasso.Callback {
                override fun onSuccess() {
                    val imageBitmap =
                        (v.phone_image.getDrawable() as BitmapDrawable).bitmap
                    val imageDrawable =
                        RoundedBitmapDrawableFactory.create(resources, imageBitmap)
                    imageDrawable.isCircular = true
                    imageDrawable.cornerRadius = Math.max(
                        imageBitmap.width,
                        imageBitmap.height
                    ) / 2.0f
                    v.phone_image.setImageDrawable(imageDrawable)
                    v.photo_progress_bar.visibility = GONE
                }

                override fun onError() {
                    TODO("Not yet implemented")
                }

            })
        viewModel.getClientUpdated()?.observe(viewLifecycleOwner, Observer { it->
            if(it != null){
                if(must_redirect) {
                    ViewUtil.setSnackBar(v, R.color.orange, "Teléfono Guardado Correctamente")
                    v.findNavController().navigate(R.id.action_cellPhoneFragment_to_addressFragment)
                }
            }else{
                ViewUtil.setSnackBar(v,R.color.red,"Ops! Hubo un problema, intenta luego nuevamente")

            }

        })

        v.button_next.setOnClickListener(View.OnClickListener {
            if(field.text.isNullOrEmpty()){
                v.tilo_cellphone.error = getString(R.string.required)
            }else{
                must_redirect = true
                viewModel.startUpdatePhone(v.context,field.text.toString(),
                viewModel.returnToken()?.value?.access_token!!)

            }
        })
        viewModel.init(v.context)
    }


}