package com.axelfernandez.deliverylavalle.ui.orderListing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.AddressAdapter
import com.axelfernandez.deliverylavalle.adapters.OrderListingAdapter
import com.axelfernandez.deliverylavalle.models.Order
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.facebook.shimmer.ShimmerFrameLayout
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.app_bar.view.*

class OrderListingFragment : Fragment() {

    private lateinit var orderListingViewModel: OrderListingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderListingViewModel = ViewModelProviders.of(this).get(OrderListingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = LoginUtils.getUserFromSharedPreferences(requireContext())
        val v = view?:return
        val loadingOrders = v.findViewById(R.id.order_listing_shimmer) as ShimmerFrameLayout
        loadingOrders.isVisible = true
        loadingOrders.startShimmer()
        v.app_bar_1.text = getString(R.string.order_listing_app_bar_1)
        v.app_bar_2.text = getString(R.string.order_listing_app_bar_2)
        orderListingViewModel.getOrders(user.token)

        orderListingViewModel.returnOrders().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            loadingOrders.stopShimmer()
            loadingOrders.isVisible = false
            val ordersInCurse = v.findViewById(R.id.order_listing_rv_in_course) as RecyclerView
            val ordersCompleted = v.findViewById(R.id.order_listing_rv_completed) as RecyclerView
            val listOfOrders = orderListingViewModel.returnListOfOrders(it)
            val listOfInCourse =listOfOrders.get("inCourse")?:return@Observer
            val listOfCompleted =listOfOrders.get("completed")?:return@Observer

            val animation: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down)
            ordersInCurse.layoutAnimation = animation
            ordersInCurse.hasFixedSize()
            ordersInCurse.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            ordersInCurse.adapter = OrderListingAdapter(listOfInCourse,requireContext(),{onClickOrderListing(it)})

            ordersCompleted.layoutAnimation = animation
            ordersInCurse.hasFixedSize()
            ordersCompleted.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            ordersCompleted.adapter = OrderListingAdapter(listOfCompleted,requireContext(),{onClickOrderListing(it)})

        })



    }

    private fun onClickOrderListing(order: Order){
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.arguments_orders), order)
        findNavController().navigate(R.id.action_navigation_dashboard_to_orderStatusDetail, bundle)
    }
}