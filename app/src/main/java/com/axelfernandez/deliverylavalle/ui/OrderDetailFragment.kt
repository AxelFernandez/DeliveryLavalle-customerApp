package com.axelfernandez.deliverylavalle.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.utils.ExitWithAnimation
import com.axelfernandez.deliverylavalle.utils.exitCircularReveal
import com.axelfernandez.deliverylavalle.utils.startCircularReveal

class OrderDetailFragment : Fragment() , ExitWithAnimation {
    override var posX: Int? = null
    override var posY: Int? = null

    override var isToBeExitedWithAnimation: Boolean = true

    companion object {
        @JvmStatic
        fun newInstance(exit: IntArray? = null): OrderDetailFragment = OrderDetailFragment().apply {
            if (exit != null && exit.size == 2) {
                posX = exit[0]
                posY = exit[1]
            }
        }
    }

    private lateinit var viewModel: OrderDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startCircularReveal(false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if ((this as? ExitWithAnimation)?.isToBeExitedWithAnimation == true) {
            if (this.posX == null || this.posY == null) {
                super.onDestroyView()
            } else {
                this.view?.exitCircularReveal(this.posX!!, this.posY!!) {
                    super.onDestroyView()
                } ?: super.onDestroyView()

            }
        } else {
            super.onDestroyView()
        }
    }

}