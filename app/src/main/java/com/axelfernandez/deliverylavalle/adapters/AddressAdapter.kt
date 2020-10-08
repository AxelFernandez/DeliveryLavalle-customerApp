package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Address

class AddressAdapter(
    var address: List<Address>  = ArrayList(),
    var context: Context,
    val itemClickListener: (Address) -> Unit,
    val deleteItemClickListener: (Address) -> Unit,
    val showDelete : Boolean = true

) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AddressViewHolder(layoutInflater.inflate(R.layout.item_delivery_address,parent,false))
    }

    override fun getItemCount(): Int {
        return address.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val item : Address = address[position]
        holder.bind(item,context, itemClickListener,deleteItemClickListener, showDelete)
    }
    class AddressViewHolder (itemView : View): RecyclerView.ViewHolder(itemView){
        var streetAndNumber : TextView = itemView.findViewById(R.id.item_delivery_address_street_number) as TextView
        var floor : TextView = itemView.findViewById(R.id.item_delivery_address_floor) as TextView
        var district : TextView = itemView.findViewById(R.id.item_delivery_address_district) as TextView
        var reference : TextView = itemView.findViewById(R.id.item_delivery_address_reference) as TextView
        var frameLayout : LinearLayout =  itemView.findViewById(R.id.item_delivery_address_layout) as LinearLayout
        var delete : ImageView =  itemView.findViewById(R.id.item_delivery_address_delete) as ImageView



        fun bind(address : Address, context: Context,
                 itemClickListener: (Address) -> Unit,
                 deleteItemClickListener: (Address) -> Unit,
                 showDelete: Boolean
        ){
            streetAndNumber.text = context.getString(R.string.item_street_and_number, address.street, address.number)
            floor.text = address.floor
            district.text = context.getString(R.string.item_district, address.district)
            reference.text = context.getString(R.string.item_reference, address.reference)
            frameLayout.setOnClickListener { itemClickListener(address) }
            if(!showDelete){
                delete.isVisible = false
            }
            delete.setOnClickListener { deleteItemClickListener(address) }

        }
    }
}
