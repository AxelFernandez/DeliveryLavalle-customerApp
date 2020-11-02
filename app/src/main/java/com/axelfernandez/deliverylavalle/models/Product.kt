package com.axelfernandez.deliverylavalle.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id :String,
    val name :String,
    val description :String,
    val photo :String,
    val price :String,
    val category :String
): Parcelable

@Parcelize
class Products: ArrayList<Product>(), Parcelable

data class ProductRequest(
    val companyId :String,
    val category :String?
)

data class ProductCategory(
    val description: String
)
@Parcelize
data class ProductDetail(
    val id :String,
    val quantity: String,
    val description: String,
    val subtotal: String
) : Parcelable
