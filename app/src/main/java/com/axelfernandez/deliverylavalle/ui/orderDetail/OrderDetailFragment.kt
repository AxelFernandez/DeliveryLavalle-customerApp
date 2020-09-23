package com.axelfernandez.deliverylavalle.ui.orderDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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


    var runnable : Runnable = Runnable {
        val cx: Int = v.getRight()
        val cy: Int = v.getBottom()
        val finalRadius: Int = Math.max(v.getWidth(), v.getHeight())
        val animator = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius.toFloat())
        animator.duration = 300
        animator.start()
    }

    lateinit var v : View
    private lateinit var productRv : RecyclerView

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
        v.post(runnable);

        var bundle : Bundle = arguments?:return
        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel::class.java)
        viewModel.initial(requireContext(),bundle)
        productRv = v.findViewById(R.id.rv_order_detail) as RecyclerView

        viewModel.returnToken().observe(viewLifecycleOwner, Observer {
            viewModel.getCompanyById(it.access_token,bundle.getString(getString(R.string.arguments_company),"null"))

        })
        viewModel.gropuped.observe(viewLifecycleOwner, Observer {
            val productsDetails : ArrayList<ProductDetail> = ArrayList()
            var total :Int = 0
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
            v.order_detail_total.text = requireContext().resources.getString(R.string.order_detail_price).format(total)
            productRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            productRv.adapter = OrderDetailAdapter(productsDetails,requireContext())
        })
        viewModel.returnCompany().observe(viewLifecycleOwner, Observer {
            v.order_detail_company_title.text = it.name
            Picasso.with(context).load(it.photo).into(v.order_detail_image_company)
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        v.removeCallbacks(runnable);
    }

}