package com.axelfernandez.deliverylavalle.ui.OrderSelectPayment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.AddressAdapter
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.ui.address.AddressViewModel
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_delivery_address.view.*

class OrderSelectPaymentAndAddress : Fragment() {

    companion object {
        fun newInstance() =
            OrderSelectPaymentAndAddress()
    }

    private lateinit var methodsRv: RecyclerView
    private lateinit var viewModel: AddressViewModel
    lateinit var v: View
    lateinit var listOfAddress: ArrayList<Address>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.order_select_payment_and_adress_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        viewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        viewModel.init(requireContext())
        val address = LoginUtils.getDefaultAddress(requireContext())
        methodsRv = v.findViewById(R.id.payment_and_delivery_rv) as RecyclerView
        methodsRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        listOfAddress = ArrayList()
        listOfAddress.add(address)
        methodsRv.adapter = AddressAdapter(listOfAddress,requireContext(),{onItemClickListener(it)}, {{}},false)




    }

    private fun onItemClickListener(address: Address){
        val bundle = Bundle()
        bundle.putParcelable("arguments_address", address)
        v.findNavController().navigate(R.id.action_orderSelectPaymentAndAddress_to_addressList, bundle)
    }
}