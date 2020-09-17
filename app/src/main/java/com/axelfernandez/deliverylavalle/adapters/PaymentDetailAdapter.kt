package com.axelfernandez.deliverylavalle.adapters

import android.content.ClipDescription
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Company
import com.squareup.picasso.Picasso

class PaymentDetailAdapter (
    var methods: List<String>  = ArrayList(),
    var context: Context,
    var showText : Boolean

): RecyclerView.Adapter<PaymentDetailAdapter.PaymentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PaymentViewHolder(layoutInflater.inflate(R.layout.item_payment_and_delivery,parent,false))
    }

    override fun getItemCount(): Int {
        return methods.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val item : String = methods[position]
        holder.bind(item, context)
        if (!showText){
            holder.description.isVisible = false
        }
    }

    class PaymentViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var image : ImageView = itemView.findViewById(R.id.item_company_payment_and_delivery_image)
        var description : TextView = itemView.findViewById(R.id.item_company_payment_and_delivery_description)

        fun bind(item: String, context: Context){
            var resource : Int = R.drawable.ic_launcher_foreground
            when (item){
                "Entrega por Delivery" -> resource = R.drawable.delivery_method_delivery
                "Entrega en el Local" -> resource = R.drawable.delivery_method_local
                "Mercado Pago" -> resource = R.drawable.credit_card
                "Efectivo" -> resource = R.drawable.cash
            }
            Picasso.with(context).load(resource).into(image)
            description.text = item

        }
    }
}