package com.axelfernandez.deliverylavalle.ui.companyDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.PaymentDetailAdapter
import com.axelfernandez.deliverylavalle.adapters.ProductCategoryAdapter
import com.axelfernandez.deliverylavalle.adapters.ProductsAdapter
import com.axelfernandez.deliverylavalle.models.*
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment.view.*
import kotlinx.android.synthetic.main.detail_fragment.view.rating
import kotlinx.android.synthetic.main.order_status_detail_fragment.view.*
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
    private lateinit var categoryRv : RecyclerView
    private lateinit var token :String
    private lateinit var idCompany :String
    private lateinit var company : Company
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
        viewModel.getRepository(requireContext())
        idCompany = requireActivity().intent.getStringExtra(getString(R.string.company_id))?:return

        val counterFab = v.findViewById(R.id.counter_fab) as CounterFab

        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        methodsRv = v.findViewById(R.id.method_available) as RecyclerView
        productRv = v.findViewById(R.id.product_rv) as RecyclerView
        categoryRv = v.findViewById(R.id.category_rv) as RecyclerView
        viewModel.getCompanyById(idCompany)
        viewModel.getProductByCompanyId(ProductRequest(idCompany,null))
        viewModel.getProductCategoryByCompanyId(idCompany)
        viewModel.returnCompany().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            v.text_view_company_name.text = it.name
            v.rating.text = it.rating.toString()
            company = it
            v.text_view_company_description.text = it.description
            v.text_view_product_of.text = getString(R.string.product_of).format(it.name)
            v.text_view_category_of.text = getString(R.string.category_of).format(it.name)
            Picasso.with(requireContext()).load(it.photo).placeholder(requireContext().getDrawable(R.drawable.ic_abstract)).into(v.detail_image)
            methodsRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            methodsRv.adapter = PaymentDetailAdapter(it.methods,requireContext(),true)
        })
        viewModel.returnProducts().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            productRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            productRv.adapter = ProductsAdapter(it,requireContext()) { product: Product, i: Int -> addToCartOnClickListener(product,i)}
            v.shimmer_product.isVisible = false
        })
        counterFab.setOnClickListener{
            var bundle: Bundle = Bundle()
            bundle.putParcelableArrayList(getString(R.string.arguments_orders),orders)
            bundle.putString(getString(R.string.arguments_company),idCompany)
            it.findNavController().navigate(R.id.action_detailFragment_to_orderDetailFragment,bundle)
        }

        viewModel.returnProductsCategory().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            categoryRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            categoryRv.adapter = ProductCategoryAdapter(it,requireContext()){itemCategoryClickListener(it)}
        })
        v.view_all_reviews.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("company", company)
            findNavController().navigate(R.id.action_detailFragment_to_reviewsFragment, bundle)
        }
    }


    private fun addToCartOnClickListener(item: Product, quantity:Int) {
        for (i in 1..quantity){
            orders.add(item)
            counter_fab.increase()
        }
    }
    private fun itemCategoryClickListener(productCategory: ProductCategory){
        viewModel.getProductByCompanyId(ProductRequest(idCompany,productCategory.description))
        v.text_view_product_of.text = getString(R.string.product_of).format(productCategory.description)
        v.shimmer_product.isVisible = true

    }
}