package com.axelfernandez.deliverylavalle.ui.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.PaymentDetailAdapter
import com.axelfernandez.deliverylavalle.adapters.ProductsAdapter
import com.axelfernandez.deliverylavalle.models.CompanyCategoryResponse
import com.axelfernandez.deliverylavalle.models.Product
import com.facebook.shimmer.Shimmer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.shimer_company.view.*
import kotlinx.android.synthetic.main.shimer_product.view.*

class DetailFragment : Fragment() {
    private lateinit var v: View
    companion object {
        fun newInstance() =
            DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel
    private lateinit var methodsRv : RecyclerView
    private lateinit var productRv : RecyclerView
    private var orders : ArrayList<Product> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.detail_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.initial(requireContext())
        val idCompany = requireActivity().intent.getStringExtra(getString(R.string.company_id))?:return
        var counterFab = v.findViewById(R.id.counter_fab) as CounterFab


        methodsRv = v.findViewById(R.id.payment_and_delivery_rv) as RecyclerView
        productRv = v.findViewById(R.id.product_rv) as RecyclerView
        viewModel.returnToken().observe(viewLifecycleOwner, Observer {
            val token = it.access_token
            viewModel.getCompanyById(token, idCompany)
            viewModel.getProductByCompanyId(token,idCompany)
        })
        viewModel.returnCompany().observe(viewLifecycleOwner, Observer {
            v.text_view_company_name.text = it.name
            v.text_view_company_description.text = it.description
            v.text_view_product_of.text = getString(R.string.product_of).format(it.name)
            Picasso.with(requireContext()).load(it.photo).into(v.detail_image)
            methodsRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            methodsRv.adapter = PaymentDetailAdapter(it.methods,requireContext(),true)
        })
        viewModel.returnProducts().observe(viewLifecycleOwner, Observer {
            productRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            productRv.adapter = ProductsAdapter(it,requireContext()) { product: Product, i: Int -> addToCartOnClickListener(product,i)}
            v.shimmer_product.isVisible = false
        })

    }
    fun addToCartOnClickListener(item: Product, quantity:Int) {
        for (i in 1..quantity){
            orders.add(item)
            counter_fab.increase()
        }

    }

}