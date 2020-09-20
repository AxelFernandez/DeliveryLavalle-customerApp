package com.axelfernandez.deliverylavalle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.axelfernandez.deliverylavalle.utils.ExitWithAnimation
import com.axelfernandez.deliverylavalle.utils.exitCircularReveal

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        val navController = findNavController(R.id.order_fragment)
    }
}