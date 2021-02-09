package com.axelfernandez.deliverylavalle.ui.orderStatusDetail


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.axelfernandez.deliverylavalle.R
import com.axelfernandez.deliverylavalle.api.Api
import com.axelfernandez.deliverylavalle.api.RetrofitFactory
import com.axelfernandez.deliverylavalle.models.MeliLink
import com.axelfernandez.deliverylavalle.models.Order
import com.axelfernandez.deliverylavalle.models.Review
import com.axelfernandez.deliverylavalle.repository.OrdersRepository
import com.kofigyan.stateprogressbar.StateProgressBar
import javax.inject.Inject

class OrderStatusDetailViewModel : ViewModel() {


    private lateinit var orderRepository : OrdersRepository
    fun getRepository(context: Context){
        orderRepository  = OrdersRepository(RetrofitFactory.buildService(Api::class.java,context))
    }

    fun getStepName(order: Order): ArrayList<String> {
        var result  = ArrayList<String>()

        when {
            order.state== "Cancelado" -> {
                result.add("Cancelado")
            }
            order.retryInLocal -> {
                result.add("Pendiente")
                result.add("En\npreparación")
                result.add("Listo\npara\nRetirar")
                result.add("Entregado")
            }
            else -> {
                result.add("Pendiente")
                result.add("En Preparacion")
                result.add("En Camino")
                result.add("Entregado")
            }
        }
        return result
    }

    fun getSteps(size :Int): StateProgressBar.StateNumber {
        return when (size){
            1 -> StateProgressBar.StateNumber.ONE
            2 -> StateProgressBar.StateNumber.TWO
            3 -> StateProgressBar.StateNumber.THREE
            4 -> StateProgressBar.StateNumber.FOUR
            else -> StateProgressBar.StateNumber.ONE
        }

    }
    fun setCurrentStepsAndStep(state :String): StateProgressBar.StateNumber {
        return when (state){
            "Pendiente" -> StateProgressBar.StateNumber.ONE
            "En preparación" -> StateProgressBar.StateNumber.TWO
            "En Camino" -> StateProgressBar.StateNumber.THREE
            "Listo para Retirar" -> StateProgressBar.StateNumber.THREE
            "Entregado" -> StateProgressBar.StateNumber.FOUR
            "Cancelado" -> StateProgressBar.StateNumber.ONE
            else -> StateProgressBar.StateNumber.ONE
        }

    }


    fun getOrders(orderId: String){
        orderRepository.getOrdersById(orderId)
    }

    fun returnOrders(): LiveData<Order> {
        return orderRepository.returnOrdersById()
    }


    fun getImageDrawable(order: Order): Int {
        //TODO: Refactor Strings to Constants
        return when (order.state){
             "Pendiente" -> R.drawable.pending
             "En Camino" -> {
                  if (order.company.category == "Comida"){
                     R.drawable.on_way_food
                 }else{
                     R.drawable.on_way_other
                 }
             }
             "Listo para Retirar" -> R.drawable.retry_in_local
             "Entregado" -> R.drawable.delivered
             "Cancelado" -> R.drawable.cancel
             "En preparación" ->{
                 if (order.company.category == "Comida"){
                     R.drawable.cooking
                 }else{
                     R.drawable.prepraing
                 }
             }
             else -> R.drawable.delivered
         }
    }
    fun getTitleName(order: Order):String{
        return when (order.state){
            "Pendiente" -> return "Tu pedido esta Pendiente"
            "En Camino" -> return "Tu pedido está en camino"
            "Listo para Retirar" -> return "Ya podés ir retirar tu pedido en la Siguiente dirección"
            "Entregado" -> "Entregamos tu pedido"
            "Cancelado" -> "Tu Pedido ha sido Cancelado"
            "En preparación" -> "Tu pedido esta siendo preparado"
            else -> return ""
        }
    }

    fun getBannerText(order: Order):String {
        return when (order.state){
            "Pendiente" -> "El pedido esta siendo revisado por el vendedor, te Informaremos las novedades"
            "En preparación" -> "Tu pedido fue confirmado, estamos preparandolo"
            "En Camino" -> "El repartidor estará en tu domicilio en breve"
            "Listo para Retirar" -> "El vendedor ha informado que poder ir a retirarlo a la siguiente direccion %s, tambien podes comunicarte con el vendedor al siguiente numero de telefono %s".format(order.company.address,order.company.phone)
            "Entregado" -> "Esperamos que hayas disfrutado de tu pedido!"
            "Cancelado" -> "El vendedor puede cancelar tu pedido si no tiene disponible lo que pediste, o porque no pudo atender tu pedido, te pedimos disculpas, y vuelve a intentarlo en otro momento"
            else -> ""
        }
    }

    fun getMeliLink(order: String){
        orderRepository.getMeliLinkByOrderId(order)
    }
    fun returnMeliLink(): LiveData<MeliLink> {
        return orderRepository.returnMeliLink()
    }

    fun getVisibleCompanyInfo(order: Order): Boolean {
        return order.state != "Entregado"
    }

    fun fetchReview(order:String){
        orderRepository.getRating(order)
    }
    fun returnReview(): LiveData<Review> {
        return orderRepository.returnRating()
    }

    fun postReview(review: Review){
        orderRepository.postRating(review)
    }
    fun returnResponseRating(): LiveData<String> {
        return orderRepository.returnResponseRating()
    }

}