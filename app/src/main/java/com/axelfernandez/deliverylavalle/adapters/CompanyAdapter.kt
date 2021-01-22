package com.axelfernandez.deliverylavalle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout.HORIZONTAL
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.Company
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.order_status_detail_fragment.view.*

class CompanyAdapter(
    var company: List<Company>  = ArrayList(),
    var context:Context,
    val companyOnClickListener: (Company) -> Unit

):RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CompanyViewHolder(layoutInflater.inflate(R.layout.company_item,parent,false))
    }

    override fun getItemCount(): Int {
        return company.size
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        var item : Company = company[position]
        holder.bind(item,context, companyOnClickListener)
        val initlayoutManager = LinearLayoutManager(holder.methods.context, HORIZONTAL, false)
        holder.methods.apply {
            layoutManager = initlayoutManager
            adapter = PaymentDetailAdapter(item.methods,context,false)
            setRecycledViewPool(viewPool)
        }
    }

    class CompanyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var image : ImageView = itemView.findViewById(R.id.company_item_image)
        var cardView : CardView = itemView.findViewById(R.id.company_item_cardview)
        var title : TextView = itemView.findViewById(R.id.company_item_title)
        var description : TextView = itemView.findViewById(R.id.company_item_subtitle)
        var rating : TextView = itemView.findViewById(R.id.rating)
        var methods : RecyclerView = itemView.findViewById(R.id.methods_rv)


        fun bind(company: Company,context: Context, companyOnClickListener: (Company) -> Unit){
            Picasso.with(context).load(company.photo).placeholder(context.getDrawable(R.drawable.ic_abstract)).into(image)
            cardView.setOnClickListener { companyOnClickListener(company) }
            rating.text = company.rating.toString()
            title.text = company.name
            description.text = company.description


        }
    }

}