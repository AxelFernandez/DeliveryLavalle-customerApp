package com.axelfernandez.deliverylavalle.ui.orderDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.OrderDetailAdapter
import com.axelfernandez.deliverylavalle.adapters.ProductsAdapter
import com.axelfernandez.deliverylavalle.models.Product
import com.axelfernandez.deliverylavalle.models.ProductDetail
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.order_detail_company_item.view.*
import kotlinx.android.synthetic.main.order_detail_fragment.view.*


class OrderDetailFragment : Fragment() {



    lateinit var v : View
    private lateinit var productRv : RecyclerView
    lateinit var idCompany: String

    companion object {
        fun newInstance() =
            OrderDetailFragment()
    }

    private lateinit var viewModel: OrderDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.order_detail_fragment, container, false)
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var total :Int = 0
        val productsDetails : ArrayList<ProductDetail> = ArrayList()
        var bundle : Bundle = arguments?:return
        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel::class.java)
        viewModel.initial(requireContext(),bundle)
        idCompany = bundle.getString(getString(R.string.arguments_company),"null")
        productRv = v.findViewById(R.id.rv_order_detail) as RecyclerView
        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        viewModel.returnToken().observe(viewLifecycleOwner, Observer {
            viewModel.getCompanyById(it.access_token,idCompany)

        })
        viewModel.gropuped.observe(viewLifecycleOwner, Observer {
            it.iterator().forEach {item ->
                var subtotal :Int = 0
                item.value.forEach { product ->
                    subtotal += product.price.toInt()
                }
                var productDetail = ProductDetail(
                    description = item.key,
                    quantity = getString(R.string.order_detail_quantity, item.value.size.toString()),
                    subtotal = subtotal.toString()
                )
                total += subtotal

                productsDetails.add(productDetail)
            }
            if (total == 0){
                v.order_detail_continue_button.isVisible = false
                v.order_detail_layout_total.isVisible = false
                v.order_detail_line_separator.isVisible = false
                v.order_detail_order_empty.isVisible = true
            }
            v.order_detail_total.text = requireContext().resources.getString(R.string.order_detail_price).format(total)
            productRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            productRv.adapter = OrderDetailAdapter(productsDetails,requireContext())
        })
        viewModel.returnCompany().observe(viewLifecycleOwner, Observer {
            v.order_detail_company_title.text = it.name
            Picasso.with(context).load(it.photo).into(v.order_detail_image_company)
        })
        v.order_detail_continue_button.setOnClickListener {
            val bundleToNav = Bundle()
            bundleToNav.putParcelableArrayList(getString(R.string.arguments_orders), productsDetails)
            bundleToNav.putInt(getString(R.string.arguments_total), total)
            bundleToNav.putString(getString(R.string.arguments_company), idCompany)
            findNavController().navigate(R.id.orderSelectPaymentAndAddress, bundle)
        }
    }


}