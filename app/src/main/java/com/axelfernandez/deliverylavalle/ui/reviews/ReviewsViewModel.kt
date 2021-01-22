package com.axelfernandez.deliverylavalle.ui.reviews

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

	private val orderRepository = OrdersRepository(RetrofitFactory.buildService(Api::class.java))

	fun bind(view : View, company: Company){
		view.company_rating.text = company.rating.toString()
		view.company_rating.isEnabled = false

	}

	fun getReviews(token:String, idCompany:String){
		orderRepository.getReviews(token, idCompany)
	}

	fun returnReviews(): LiveData<List<Review>> {
		return orderRepository.returnReviews()
	}
}