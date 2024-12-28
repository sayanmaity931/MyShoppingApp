package com.example.myshoppingappuser.domain_layer.models

data class CartModel(
    var productId : String = "",
    val productName : String = "",
    val productDescription : String = "",
    val productQty : String = "",
    val productPrice : String = "",
    val productFinalPrice : String = "",
    val productCategory : String = "",
    val productImageUrl : String = "",
    val color : String = "",
    val size : String = "",
    val date : Long = System.currentTimeMillis()
)
