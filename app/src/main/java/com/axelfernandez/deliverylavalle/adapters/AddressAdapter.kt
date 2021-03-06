package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
    val editItemClickListener: (Address) -> Unit,
    val showEditables : Boolean = true,
    val showArrow : Boolean = true

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
        holder.bind(item,context, itemClickListener,deleteItemClickListener, editItemClickListener, showEditables, showArrow)
    }
    class AddressViewHolder (itemView : View): RecyclerView.ViewHolder(itemView){
        var streetAndNumber : TextView = itemView.findViewById(R.id.item_delivery_address_street_number) as TextView
        var floor : TextView = itemView.findViewById(R.id.item_delivery_address_floor) as TextView
        var district : TextView = itemView.findViewById(R.id.item_delivery_address_district) as TextView
        var reference : TextView = itemView.findViewById(R.id.item_delivery_address_reference) as TextView
        var frameLayout : LinearLayout =  itemView.findViewById(R.id.item_delivery_address_layout) as LinearLayout
        var delete : ImageView =  itemView.findViewById(R.id.item_delivery_address_delete) as ImageView
        var edit : ImageView =  itemView.findViewById(R.id.item_delivery_address_edit) as ImageView
        var arrowFrame : FrameLayout =  itemView.findViewById(R.id.frame_arrow) as FrameLayout



        fun bind(address : Address, context: Context,
                 itemClickListener: (Address) -> Unit,
                 deleteItemClickListener: (Address) -> Unit,
                 editItemClickListener: (Address) -> Unit,
                 showEditables: Boolean,
                 showArrow: Boolean
        ){
            streetAndNumber.text = context.getString(R.string.item_street_and_number, address.street, address.number)
            floor.text = address.floor
            district.text = context.getString(R.string.item_district, address.district)
            reference.text = context.getString(R.string.item_reference, address.reference)
            frameLayout.setOnClickListener { itemClickListener(address) }
            delete.isVisible = showEditables
            edit.isVisible = showEditables
            arrowFrame.isVisible = showArrow
            delete.setOnClickListener { deleteItemClickListener(address) }
            edit.setOnClickListener { editItemClickListener(address) }
        }
    }
}
