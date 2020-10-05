package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Address

class AddressAdapter(
    var address: List<Address>  = ArrayList(),
    var context: Context,
    val itemClickListener: (Address) -> Unit

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
        holder.bind(item,context, itemClickListener)
    }
    class AddressViewHolder (itemView : View): RecyclerView.ViewHolder(itemView){
        var streetAndNumber : TextView = itemView.findViewById(R.id.item_delivery_address_street_number) as TextView
        var floor : TextView = itemView.findViewById(R.id.item_delivery_address_floor) as TextView
        var district : TextView = itemView.findViewById(R.id.item_delivery_address_district) as TextView
        var reference : TextView = itemView.findViewById(R.id.item_delivery_address_reference) as TextView
        var frameLayout : LinearLayout =  itemView.findViewById(R.id.item_delivery_address_layout) as LinearLayout



        fun bind(address : Address, context: Context,
                 itemClickListener: (Address) -> Unit){
            streetAndNumber.text = context.getString(R.string.item_street_and_number, address.street, address.number)
            floor.text = address.floor
            district.text = address.district
            reference.text = address.reference
            frameLayout.setOnClickListener { itemClickListener(address) }
        }
    }
}
