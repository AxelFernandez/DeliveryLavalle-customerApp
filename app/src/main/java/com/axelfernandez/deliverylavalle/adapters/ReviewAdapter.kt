package com.axelfernandez.deliverylavalle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.models.*
import kotlinx.android.synthetic.main.order_status_detail_fragment.view.*

class ReviewAdapter (

	var review: List<Review>  = ArrayList()


) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {



	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ReviewViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		return ReviewViewHolder(
			layoutInflater.inflate(
				R.layout.item_review,
				parent,
				false
			)
		)
	}

	override fun getItemCount(): Int {
		return review.size
	}

	override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
		val item : Review = review[position]
		holder.bind(item)
	}

	class ReviewViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
		var userName : TextView = itemView.findViewById(R.id.item_user_name)
		var description : TextView = itemView.findViewById(R.id.item_review)
		var rating : TextView = itemView.findViewById(R.id.item_rating)

		fun bind(review: Review){
			rating.text = review.rating
			userName.text = review.userName
			description.text = review.description


		}
	}


}