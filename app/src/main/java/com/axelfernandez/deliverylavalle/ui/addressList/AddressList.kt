package com.axelfernandez.deliverylavalle.ui.addressList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.widget.ViewUtils
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.AddressAdapter
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.ui.address.AddressViewModel
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import kotlinx.android.synthetic.main.address_list_fragment.view.*
import kotlinx.android.synthetic.main.item_delivery_address.view.*

class AddressList : Fragment() {

    companion object {
        fun newInstance() = AddressList()
    }

    private lateinit var addresListRv: RecyclerView
    private lateinit var v: View
    private lateinit var viewModel: AddressViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v =  inflater.inflate(R.layout.address_list_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addresListRv = v.findViewById(R.id.rv_addresses) as RecyclerView
        viewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        viewModel.init(requireContext())
        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        viewModel.returnToken().observe(viewLifecycleOwner, Observer {
            viewModel.soliciteAddress(it.access_token)
            token = it.access_token
        })
        viewModel.notifyAddres().observe(viewLifecycleOwner, Observer {
            addresListRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            addresListRv.adapter = AddressAdapter(it,requireContext(), {onItemClickListener(it)}, {deleteOnItemClickListener(it)})
            v.addres_list_progress_bar.isVisible = false

        })
        v.add_new_address.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(getString(R.string.argument_must_return),true)
            v.findNavController().navigate(R.id.action_addressList_to_addressFragment2, bundle)
        }

        viewModel.notifyCorrectDeleted().observe(viewLifecycleOwner, Observer {
            ViewUtil.setSnackBar(v,R.color.green,"Eliminado correctamente")
            viewModel.soliciteAddress(token)
        })
    }
    private fun onItemClickListener(address: Address) {
        LoginUtils.saveDefaultAddress(requireContext(),address)
        v.findNavController().popBackStack()
    }
    private fun deleteOnItemClickListener(address: Address){
        val savedAddress = LoginUtils.getDefaultAddress(requireContext())
        if(savedAddress.street == address.street){
            LoginUtils.removeAddress(requireContext())
        }
        viewModel.deleteAddress(address, token)
    }
}