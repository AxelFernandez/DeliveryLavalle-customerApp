package com.axelfernandez.deliverylavalle.ui.OrderSelectPayment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toolbar
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.AddressAdapter
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.PaymentMethods
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.ui.address.AddressViewModel
import com.axelfernandez.deliverylavalle.ui.companyDetail.DetailViewModel
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.item_delivery_address.view.*
import kotlinx.android.synthetic.main.order_select_payment_and_adress_fragment.*
import kotlinx.android.synthetic.main.order_select_payment_and_adress_fragment.view.*
import java.util.concurrent.atomic.AtomicBoolean

class OrderSelectPaymentAndAddress : Fragment() {

    companion object {
        fun newInstance() =
            OrderSelectPaymentAndAddress()
        var atomicBoolean = AtomicBoolean()

    }


    private lateinit var methodsRv: RecyclerView
    private lateinit var paymentMethods: PaymentMethods
    private lateinit var viewModel: AddressViewModel
    private lateinit var viewModelCompany: OrderSelectPaymentAndAddressViewModel
    private lateinit var v: View
    private lateinit var listOfAddress: ArrayList<Address>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.order_select_payment_and_adress_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity?:return).get(AddressViewModel::class.java)
        viewModelCompany = ViewModelProviders.of(activity?:return).get(OrderSelectPaymentAndAddressViewModel::class.java)
        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        val user : User = LoginUtils.getUserFromSharedPreferences(requireContext())
        val bundle = arguments?:return
        val companyId = bundle.getString(getString(R.string.arguments_company))?:return
        val address = LoginUtils.getDefaultAddress(requireContext())
        methodsRv = v.findViewById(R.id.payment_and_delivery_rv) as RecyclerView
        methodsRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        listOfAddress = ArrayList()
        listOfAddress.add(address)
        methodsRv.adapter = AddressAdapter(listOfAddress,requireContext(),{onItemClickListener(it)}, {{}},false)
        var atomicBoolean = AtomicBoolean()
        if (atomicBoolean.compareAndSet(false, true)){
            viewModelCompany.solicitPaymentMethod(user.token, companyId)
        }

        viewModelCompany.returnPaymentMethod().observe(viewLifecycleOwner, Observer {
            paymentMethods = it
            it.methods.forEachIndexed(){index,item->
                val radioButton1 = RadioButton(requireContext())
                radioButton1.setText(item)
                radioButton1.id = index
                v.radioGroup.addView(radioButton1)
            }
        })
        v.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                1 -> v.phishing_banner_1.isVisible = true
                else -> v.phishing_banner_1.isVisible = false
            }
        }
        v.order_detail_continue_button.setOnClickListener {

            arguments?.putParcelable(getString(R.string.arguments_address),LoginUtils.getDefaultAddress(requireContext()))
            arguments?.putString(getString(R.string.arguments_method),paymentMethods.methods[radioGroup.checkedRadioButtonId])


        }
    }

    private fun onItemClickListener(address: Address){
        val bundle = arguments?:Bundle()
        bundle.putParcelable("arguments_address", address)
        v.findNavController().navigate(R.id.action_orderSelectPaymentAndAddress_to_addressList, bundle)
    }
}