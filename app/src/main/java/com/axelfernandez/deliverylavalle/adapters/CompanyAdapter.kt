package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.CompanyCategoryResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comapny_item.view.*

class CompanyAdapter():RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    var company : List<Company> = ArrayList()
    lateinit var context: Context

    fun CompanyAdapter(context: Context, company : List<Company>){
        this.company = company
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CompanyViewHolder(layoutInflater.inflate(R.layout.comapny_item,parent,false))
    }

    override fun getItemCount(): Int {
        return company.size
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        var item : Company = company[position]
        holder.bind(item,context)
    }

    class CompanyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var image : ImageView = itemView.findViewById(R.id.company_item_image)
        var cardView : CardView = itemView.findViewById(R.id.company_item_cardview)
        var title : TextView = itemView.findViewById(R.id.company_item_title)
        var description : TextView = itemView.findViewById(R.id.company_item_subtitle)
        var delivery : ImageView = itemView.findViewById(R.id.company_item_delivery_method_delivery)
        var local : ImageView = itemView.findViewById(R.id.company_item_delivery_method_local)
        var creditCard : ImageView = itemView.findViewById(R.id.company_item_payment_method_credit_card)
        var cash : ImageView = itemView.findViewById(R.id.company_item_payment_method_cash)

        fun bind(company: Company,context: Context){
            Picasso.with(context).load(company.photo).into(image)
            cardView.setOnClickListener {
                //it.findNavController().navigate() TODO: Navigate to Detail Company
            }
            title.text = company.name
            description.text = company.description
            company.deliveryMethod.forEach { item ->
                when(item){
                    "Entrega por Delivery" -> delivery.visibility = VISIBLE
                    "Entrega en el Local" -> local.visibility = VISIBLE
                }

            }
            company.paymentMethod.forEach{
                when(it){
                    "Mercado Pago" -> creditCard.visibility= VISIBLE
                    "Efectivo" -> cash.visibility= VISIBLE

                }
            }

        }
    }

}