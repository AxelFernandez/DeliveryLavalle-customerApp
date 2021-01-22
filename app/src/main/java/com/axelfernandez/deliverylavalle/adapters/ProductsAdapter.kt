package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.Product
import com.shawnlin.numberpicker.NumberPicker
import com.squareup.picasso.Picasso


class ProductsAdapter (
    var product: List<Product>  = ArrayList(),
    var context: Context,
    val addToCartOnClickListener: (Product, Int) -> Unit



): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_products,parent,false))
    }

    override fun getItemCount(): Int {
        return product.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item : Product = product[position]
        holder.bind(item, context,addToCartOnClickListener)
    }

    class ProductViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var imageView: ImageView = itemView.findViewById(R.id.item_product_image)
        var title: TextView = itemView.findViewById(R.id.item_product_title)
        var subtitle: TextView = itemView.findViewById(R.id.item_product_subtitle)
        var price: TextView = itemView.findViewById(R.id.item_product_price)
        var picker: NumberPicker = itemView.findViewById(R.id.number_picker)
        var button: Button = itemView.findViewById(R.id.item_product_add_to_order)

        fun bind(product: Product,context:Context, addToCartOnClickListener: (Product, Int) -> Unit){
            Picasso.with(context).load(product.photo).placeholder(context.getDrawable(R.drawable.ic_abstract)).into(imageView)
            title.text = product.name
            subtitle.text = product.description
            price.text = context.resources.getString(R.string.order_detail_price, product.price)
            button.setOnClickListener {
                addToCartOnClickListener(product,picker.value)
            }




        }
    }
}