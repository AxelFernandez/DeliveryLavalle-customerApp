package com.axelfernandez.deliverylavalle.ui.reviews

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.Company
import com.axelfernandez.deliverylavalle.models.Review
import com.axelfernandez.deliverylavalle.repository.OrdersRepository
import kotlinx.android.synthetic.main.reviews_fragment.view.*

class ReviewsViewModel : ViewModel() {

	private lateinit var orderRepository : OrdersRepository
	fun getRepository(context: Context){
		orderRepository  = OrdersRepository(RetrofitFactory.buildService(Api::class.java,context))
	}

	fun bind(view : View, company: Company){
		view.company_rating.text = company.rating.toString()
		view.company_rating.isEnabled = false

	}

	fun getReviews(idCompany:String){
		orderRepository.getReviews(idCompany)
	}

	fun returnReviews(): LiveData<List<Review>> {
		return orderRepository.returnReviews()
	}
}