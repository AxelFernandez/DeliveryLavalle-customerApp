package com.axelfernandez.deliverylavalle.ui.orderStatusDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.OrderDetailAdapter
import com.axelfernandez.deliverylavalle.models.Order
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.kofigyan.stateprogressbar.StateProgressBar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.banner_info.view.*
import kotlinx.android.synthetic.main.order_detail_company_item.view.*
import kotlinx.android.synthetic.main.order_status_detail_fragment.view.*


class OrderStatusDetail : Fragment() {

    companion object {
        fun newInstance() =
            OrderStatusDetail()
    }

    private lateinit var viewModel: OrderStatusDetailViewModel
    private lateinit var meliLink: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_status_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrderStatusDetailViewModel::class.java)
        val v = view?:return
        val arguments = arguments?:return
        val orderArguments = arguments.getParcelable<Order>(getString(R.string.arguments_orders))?:return
        val user = LoginUtils.getUserFromSharedPreferences(requireContext())
        viewModel.getOrders(user.token,orderArguments.id)
        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        val stateProgressBar = v.findViewById(R.id.order_status_detail_step) as StateProgressBar
        val productRv = v.findViewById(R.id.order_status_products) as RecyclerView
        v.app_bar_1.text = getString(R.string.order_number)
        v.app_bar_2.text = orderArguments.id
        v.order_status_payment_method.text = orderArguments.paymentMethod
        var actionBarHeight = 0
        val tv = TypedValue()
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        if (orderArguments.paymentMethod == getString(R.string.mercado_pago)){
            viewModel.getMeliLink(user.token,orderArguments.id)
        }


        viewModel.returnMeliLink().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            v.order_status_banner_info_2.isVisible = true
            if (it.isAvailable){
                v.order_status_banner_info_2.banner_info_text.text= getString(R.string.meli_link_available)
                v.order_status_banner_info_2.banner_info_background.background = getDrawable(requireContext(), R.color.green)
                val param = v.order_status_scroll.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0,actionBarHeight,0,actionBarHeight)
                v.order_status_scroll.layoutParams = param
                meliLink = it.link?: return@Observer
                v.order_detail_open_meli.isVisible = true

            }else{
                v.order_status_banner_info_2.banner_info_text.text= getString(R.string.meli_link_not_available)
                v.order_status_banner_info_2.banner_info_background.background = getDrawable(requireContext(), R.color.orange)

            }
        })
        viewModel.returnOrders().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            val order = it
            val steps = viewModel.getStepName(order)
            stateProgressBar.setMaxStateNumber(viewModel.getSteps(steps.size))
            stateProgressBar.setCurrentStateNumber(viewModel.setCurrentStepsAndStep(order.state))
            stateProgressBar.setStateDescriptionData(steps)
            v.order_status_detail_title.text = viewModel.getTitleName(order)
            v.order_status_banner_info.banner_info_text.text = viewModel.getBannerText(order)
            v.order_status_detail_image.setImageResource(viewModel.getImageDrawable(order))
            v.order_status_total.text = getString(R.string.order_detail_price).format(order.total)
            productRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            productRv.adapter = OrderDetailAdapter(order.items,requireContext())

            v.order_status_company.order_detail_company_title.text = order.company.name
            v.order_status_company.order_detail_company_adresss.text = order.company.address
            v.order_status_company.order_detail_company_phone.text = order.company.phone
            Picasso.with(requireContext()).load(order.company.photo).into(v.order_status_company.order_detail_image_company)
            v.order_status_company.order_detail_company_phone.text = order.company.phone
            v.order_status_company.isVisible = viewModel.getVisibleCompanyInfo(order)
        })
        v.order_detail_open_meli.setOnClickListener {
           try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(meliLink))
             startActivity(browserIntent)
           }catch (e : Exception ){
               ViewUtil.setSnackBar(v,R.color.orange,getString(R.string.meli_link_not_found))
           }

        }

    }

}