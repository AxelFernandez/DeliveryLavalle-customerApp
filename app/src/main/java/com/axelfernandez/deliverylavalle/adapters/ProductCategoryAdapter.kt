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
import com.axelfernandez.deliverylavalle.models.CompanyCategoryResponse
import com.axelfernandez.deliverylavalle.models.ProductCategory
import com.squareup.picasso.Picasso

class ProductCategoryAdapter(
    var category: List<ProductCategory>  = ArrayList(),
    var context: Context,
    val itemCategoryClickListener: (ProductCategory) -> Unit

) : RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductCategoryViewHolder(layoutInflater.inflate(R.layout.item_category_product,parent,false))
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: ProductCategoryViewHolder, position: Int) {
        var item : ProductCategory = category[position]
        holder.bind(item,context, itemCategoryClickListener)
    }
    class ProductCategoryViewHolder (itemView : View): RecyclerView.ViewHolder(itemView){
        var description : TextView = itemView.findViewById(R.id.item_product_category_description) as TextView
        var linearLayout : LinearLayout =  itemView.findViewById(R.id.item_product_category_ln) as LinearLayout



        fun bind(category : ProductCategory, context: Context,
                 itemCategoryClickListener: (ProductCategory) -> Unit){
            description.text = category.description
            linearLayout.setOnClickListener { itemCategoryClickListener(category) }
        }
    }
}
