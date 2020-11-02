package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.ProductDetail


class OrderDetailAdapter(
    var product: List<ProductDetail>  = ArrayList(),
    var context: Context
    ) : RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OrderDetailViewHolder(layoutInflater.inflate(R.layout.order_detail_detail_item,parent,false))
    }

    override fun getItemCount(): Int {
        return product.size
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        var item : ProductDetail = product[position]
        holder.bind(item,context)
    }
    class OrderDetailViewHolder (itemView : View): RecyclerView.ViewHolder(itemView){
        var description : TextView = itemView.findViewById(R.id.order_detail_detail_description) as TextView
        var quantity : TextView = itemView.findViewById(R.id.order_detail_detail_quantity) as TextView
        var subtotal : TextView = itemView.findViewById(R.id.order_detail_detail_subtotal) as TextView

        fun bind(item : ProductDetail, context: Context){
            description.text = item.description
            quantity.text = item.quantity
            subtotal.text = "$%s".format(item.subtotal)
        }
    }


}