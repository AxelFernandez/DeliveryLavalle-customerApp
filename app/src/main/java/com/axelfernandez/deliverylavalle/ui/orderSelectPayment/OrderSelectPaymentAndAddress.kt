package com.axelfernandez.deliverylavalle.ui.orderSelectPayment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.adapters.AddressAdapter
import com.axelfernandez.deliverylavalle.models.Address
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.PaymentMethods
import com.axelfernandez.deliverylavalle.models.User
import com.axelfernandez.deliverylavalle.ui.address.AddressViewModel
import com.axelfernandez.deliverylavalle.ui.companyDetail.DetailViewModel
import com.axelfernandez.deliverylavalle.utils.LoginUtils
import com.axelfernandez.deliverylavalle.utils.ViewUtil
import kotlinx.android.synthetic.main.banner_local_delivery.view.*
import kotlinx.android.synthetic.main.banner_phishing.view.*
import kotlinx.android.synthetic.main.order_select_payment_and_adress_fragment.*
import kotlinx.android.synthetic.main.order_select_payment_and_adress_fragment.view.*
import java.util.concurrent.atomic.AtomicBoolean

class OrderSelectPaymentAndAddress : Fragment() {

    companion object {
        fun newInstance() =
            OrderSelectPaymentAndAddress()

    }
    private var atomicBoolean = AtomicBoolean(false)
    private var atomicDeliveryBoolean = AtomicBoolean(false)
    private lateinit var methodsRv: RecyclerView
    private lateinit var paymentMethods: PaymentMethods
    private lateinit var viewModel: AddressViewModel
    private lateinit var viewModelMethods: OrderSelectPaymentAndAddressViewModel
    private lateinit var viewModelCompany: DetailViewModel
    private lateinit var v: View
    private lateinit var listOfAddress: ArrayList<Address>
    private lateinit var company: Company

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.order_select_payment_and_adress_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        viewModelMethods = ViewModelProviders.of(this).get(OrderSelectPaymentAndAddressViewModel::class.java)
        viewModelCompany = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModelCompany.getRepository(requireContext())
        viewModel.getRepository(requireContext())
        viewModelMethods.getRepository(requireContext())
        val toolbar = v.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        v.phishing_text.text = getString(R.string.phishing_text_1)
        val bundle = arguments?:return
        val companyId = bundle.getString(getString(R.string.arguments_company))?:return
        val address = LoginUtils.getDefaultAddress(requireContext())

        methodsRv = v.findViewById(R.id.payment_and_delivery_rv) as RecyclerView
        methodsRv.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        listOfAddress = ArrayList()
        listOfAddress.add(address)
        methodsRv.adapter = AddressAdapter(listOfAddress,requireContext(),{onItemClickListener(it)}, {{}},false)

        if (atomicBoolean.compareAndSet(false, true)){
            viewModelMethods.solicitPaymentMethod(companyId)
        }
        if (atomicDeliveryBoolean.compareAndSet(false, true)){
            viewModelMethods.solicitDeliveryMethod(companyId)
        }
        viewModelCompany.returnCompany().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            v.banner_local.local_delivery_address.text = it.address
            v.banner_local.local_delivery_phone.text = it.phone
        })

        v.retry_in_local_switch.setOnCheckedChangeListener{_, i->
            v.banner_local.isVisible = i
            v.payment_and_delivery_rv.isVisible = !i

        }

        viewModelMethods.returnDeliveryMethod().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
            viewModelCompany.getCompanyById(companyId)
            when (it.methods.size){
                1 ->{
                    if(it.methods.first() == "Retiro en el Local"){
                        v.banner_local.isVisible = true
                        v.payment_and_delivery_rv.isVisible = false
                    }else{
                        v.retry_in_local_switch.isVisible = false
                    }
                }
            }
        })
        viewModelMethods.returnPaymentMethod().observe(viewLifecycleOwner, Observer {
            if(it == null){
                ViewUtil.setSnackBar(v,R.color.red,getString(R.string.no_conection))
            }
            val it = it?:return@Observer
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
            if (radioGroup.checkedRadioButtonId == -1){
                ViewUtil.setSnackBar(v,R.color.red,"Debes seleccionar un metodo de pago")
                return@setOnClickListener
            }
            if(LoginUtils.getDefaultAddress(requireContext()).id == "" && !v.retry_in_local_switch.isChecked){
                ViewUtil.setSnackBar(v,R.color.red,"Debes seleccionar una direccion de entrega")
                return@setOnClickListener
            }
            val arguments = arguments?:return@setOnClickListener
            arguments.putParcelable(getString(R.string.arguments_address),LoginUtils.getDefaultAddress(requireContext()))
            arguments.putBoolean(getString(R.string.argument_retry_in_local),v.retry_in_local_switch.isChecked)
            arguments.putString(getString(R.string.arguments_method),paymentMethods.methods[radioGroup.checkedRadioButtonId])
            findNavController().navigate(R.id.action_orderSelectPaymentAndAddress_to_confirmationFragment, arguments)

        }
    }

    private fun onItemClickListener(address: Address){
        val bundle = arguments?:Bundle()
        bundle.putParcelable("arguments_address", address)
        v.findNavController().navigate(R.id.action_orderSelectPaymentAndAddress_to_addressList, bundle)
    }

    override fun onPause() {
        super.onPause()
        atomicBoolean.set(false)
        atomicDeliveryBoolean.set(false)
    }
}