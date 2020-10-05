package com.axelfernandez.deliverylavalle.ui.addressList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.AddressAdapter
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.ui.address.AddressViewModel
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import kotlinx.android.synthetic.main.address_list_fragment.view.*

class AddressList : Fragment() {

    companion object {
        fun newInstance() = AddressList()
    }

    private lateinit var methodsRv: RecyclerView
    private lateinit var v: View
    private lateinit var viewModel: AddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v =  inflater.inflate(R.layout.address_list_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        methodsRv = v.findViewById(R.id.rv_addresses) as RecyclerView
        viewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        viewModel.init(requireContext())
        viewModel.returnToken().observe(viewLifecycleOwner, Observer {
            viewModel.soliciteAddress(it.access_token)
        })
        viewModel.notifyAddres().observe(viewLifecycleOwner, Observer {
            methodsRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            methodsRv.adapter = AddressAdapter(it,requireContext()){onItemClickListener(it)}
        })
        v.add_new_address.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(getString(R.string.argument_must_return),true)
            v.findNavController().navigate(R.id.action_addressList_to_addressFragment2, bundle)
        }
    }
    private fun onItemClickListener(address: Address){
        LoginUtils.saveDefaultAddress(requireContext(),address)
        v.findNavController().popBackStack()
    }

}