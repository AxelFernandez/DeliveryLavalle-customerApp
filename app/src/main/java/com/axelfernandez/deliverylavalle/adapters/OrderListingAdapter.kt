package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.Order
import com.squareup.picasso.Picasso

class OrderListingAdapter(
    private var orders: List<Order>  = ArrayList(),
    var context: Context,
    private val addOnClickViewOrder: (Order) -> Unit
    ): RecyclerView.Adapter<OrderListingAdapter.OrderListingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OrderListingViewHolder(layoutInflater.inflate(R.layout.item_order_listing, parent,false))
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrderListingViewHolder, position: Int) {
        val item : Order = orders[position]
        holder.bind(item, context,  addOnClickViewOrder)
    }

    class OrderListingViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        private var imageView: ImageView = itemView.findViewById(R.id.item_order_listing_image)
        private var orderId: TextView = itemView.findViewById(R.id.item_order_listing_id)
        private var state: TextView = itemView.findViewById(R.id.item_order_listing_state)
        private var date: TextView = itemView.findViewById(R.id.item_order_listing_date_created)
        private var cardview: CardView = itemView.findViewById(R.id.item_order_listing_cardView)

        fun bind(order: Order, context: Context, addToCartOnClickListener: (Order) -> Unit){
            Picasso.with(context).load(order.company.photo).into(imageView)
            orderId.text = context.getString(R.string.order_listing_id ,order.id)
            state.text = context.getString(R.string.state_order_listing, order.state)
            date.text = context.getString(R.string.date_create_order_listing, order.dateCreated)
            cardview.setOnClickListener {
                addToCartOnClickListener(order)
            }




        }
    }
}