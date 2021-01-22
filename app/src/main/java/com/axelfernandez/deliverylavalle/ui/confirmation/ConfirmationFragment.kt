package com.axelfernandez.deliverylavalle.ui.confirmation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.AddressAdapter
import com.axelfernandez.deliverylavalle.adapters.OrderDetailAdapter
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.Order
import com.axelfernandez.deliverylavalle.models.OrderPost
import com.axelfernandez.deliverylavalle.models.ProductDetail
import com.axelfernandez.deliverylavalle.ui.address.AddressViewModel
import com.axelfernandez.deliverylavalle.ui.companyDetail.DetailViewModel
import com.axelfernandez.deliverylavalle.ui.login.Login
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.banner_local_delivery.view.*
import kotlinx.android.synthetic.main.banner_phishing.view.*
import kotlinx.android.synthetic.main.confirmation_fragment.view.*
import kotlinx.android.synthetic.main.order_detail_company_item.view.*
import kotlinx.android.synthetic.main.order_select_payment_and_adress_fragment.view.*

class ConfirmationFragment : Fragment() {

    companion object {
        fun newInstance() =
            ConfirmationFragment()
    }

    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var viewModelCompany: DetailViewModel
    private lateinit var viewModelAddress: AddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.confirmation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConfirmationViewModel::class.java)
        viewModelCompany = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        val v = view?:return
        val arguments = arguments?:return
        val companyId = arguments.getString(resources.getString(R.string.arguments_company))?:return
        val user = LoginUtils.getUserFromSharedPreferences(requireContext())
        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        val retryInLocal: Boolean = arguments.getBoolean(getString(R.string.argument_retry_in_local))

        //CompanyId
        viewModelCompany.getCompanyById(user.token,companyId)
        viewModelCompany.returnCompany().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            v.order_detail_company_title.text = it.name
            v.confirmation_banner_local.local_delivery_address.text = it.address
            v.confirmation_banner_local.local_delivery_phone.text = it.phone
            Picasso.with(context).load(it.photo).placeholder(requireContext().getDrawable(R.drawable.ic_abstract)).into(v.order_detail_image_company)
        })

        //Address RV
        val methodsRv = v.findViewById(R.id.confirmation_address_rv) as RecyclerView
        methodsRv.isNestedScrollingEnabled = false
        methodsRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        methodsRv.isVisible = !retryInLocal
        val listOfAddress = ArrayList<Address>()
        listOfAddress.add(LoginUtils.getDefaultAddress(requireContext()))
        methodsRv.adapter = AddressAdapter(listOfAddress,requireContext(),{}, {}, showDelete = false, showArrow = false)

        //Banner Retry In Local
        v.confirmation_banner_local.isVisible = retryInLocal

        //Payment Method
        val method = arguments.getString(getString(R.string.arguments_method))?:return
        v.confirmation_method.text = method
        if(method == getString(R.string.mercado_pago)){
            v.phishing_text.text = getString(R.string.phishing_text_2)
        }else{
            v.banner_phishing.isVisible = false
        }


        //Products
        val products = arguments.getParcelableArrayList<ProductDetail>(getString(R.string.arguments_orders_grouped)) as List<ProductDetail>
        val productRv = v.findViewById(R.id.rv_order_detail) as RecyclerView
        productRv.isNestedScrollingEnabled = false
        productRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        productRv.adapter = OrderDetailAdapter(products,requireContext())

        //Total
        val total = arguments.getInt(getString(R.string.arguments_total)).toString()
        v.confirmation_total.text = total

        //Confirm Order
        v.order_confirmation_button.setOnClickListener {
            val address = LoginUtils.getDefaultAddress(requireContext())
            // TODO: Possible security problem, replace this Total for a logic in Server
            val order: OrderPost = OrderPost(companyId,address.id, method, total,products,retryInLocal)
            val user = LoginUtils.getUserFromSharedPreferences(requireContext())
            viewModel.postOrder(order,user.token)
        }

        viewModel.returnOrders().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            arguments.putParcelable(getString(R.string.arguement_order_response), it)
            findNavController().navigate(R.id.action_confirmationFragment_to_orderDecidedFragment, arguments)


        })
    }

}