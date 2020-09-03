package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.CompanyCategoryResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_company_item.view.*

class CompanyCategotyAdapter() :RecyclerView.Adapter<CompanyCategotyAdapter.CompanyCategoryViewHolder>() {

    var category: List<CompanyCategoryResponse>  = ArrayList()
    lateinit var context:Context

    fun CompanyCategoryAdapter(context: Context, category: List<CompanyCategoryResponse>){
        this.category = category
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyCategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CompanyCategoryViewHolder(layoutInflater.inflate(R.layout.category_company_item,parent,false))
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: CompanyCategoryViewHolder, position: Int) {
        var item : CompanyCategoryResponse = category[position]
        holder.bind(item,context)
    }
    class CompanyCategoryViewHolder (itemView : View): ViewHolder(itemView){
         var description : TextView= itemView.findViewById(R.id.item_company_category_description) as TextView
         var photo : ImageView? =  itemView.findViewById(R.id.item_company_category_photo) as ImageView



        fun bind(categoty : CompanyCategoryResponse, context: Context){
            description?.text = categoty.description
            Picasso.with(context).load(categoty.photo).into(photo)
        }
    }


}