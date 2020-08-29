package com.axelfernandez.deliverylavalle.utils

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import com.axelfernandez.deliverylavalle.R
import com.google.android.material.snackbar.Snackbar

class ViewUtil {

    companion object{
        fun setSnackBar(v : View, color: Int,text:String){
            var snackbar : Snackbar = Snackbar.make(v,text,
                Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(v.context, color))
            snackbar.show()
        }

    }
}