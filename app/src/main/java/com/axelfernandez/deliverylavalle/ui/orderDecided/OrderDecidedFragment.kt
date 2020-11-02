package com.axelfernandez.deliverylavalle.ui.orderDecided

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.axelfernandez.deliverylavalle.HomeActivity
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.OrderResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.order_decided_fragment.view.*

class OrderDecidedFragment : Fragment() {

    companion object {
        fun newInstance() =
            OrderDecidedFragment()
    }

    private lateinit var viewModel: OrderDecidedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_decided_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrderDecidedViewModel::class.java)
        val v = view?:return
        val arguments = arguments?:return
        val orderResponse =  arguments.getParcelable<OrderResponse>(getString(R.string.arguement_order_response))?:return
        when (orderResponse.responseCode){
            200 ->{
                v.decided_order_background.background = getDrawable(requireContext(), R.color.green)
                //Picasso.with(requireContext()).load(getDrawable(requireContext(),R.drawable.ic_baseline_check_24)).into(v.decided_order_icon.dr)
                v.decided_order_icon.setImageDrawable(getDrawable(requireContext(),R.drawable.ic_baseline_check_24))
                Picasso.with(requireContext()).load(R.drawable.goodorder).into(v.decided_order_reaction)
                v.decided_order_title.text = "Tu pedido fue creado con éxito"
                v.decided_order_subtitle.text = "Espera la confirmación de tu pedido en el menu principal"
                v.decided_order_id_text_view.text = "#%s".format(orderResponse.orderId)
                v.decided_order_state.text = orderResponse.state
            }
            400->{
                v.decided_order_background.background = getDrawable(requireContext(), R.color.red)
                Picasso.with(requireContext()).load(R.drawable.badorder).into(v.decided_order_reaction)
                v.decided_order_icon.setImageDrawable(getDrawable(requireContext(),R.drawable.ic_baseline_clear_24))
                v.decided_order_title.text = "Tu pedido no pudo ser creado"
                v.decided_order_subtitle.text = "El vendedor acaba de cerrar y no recibe pedidos en este momento"
                v.decided_order_id.isVisible = false
                v.decided_order_state.text = orderResponse.state
            }

        }
        v.decided_return_main_menu.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }

    }

}