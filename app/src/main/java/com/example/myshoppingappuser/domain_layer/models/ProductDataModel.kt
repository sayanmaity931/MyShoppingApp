package com.example.myshoppingappuser.domain_layer.models

import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.Serializable


data class ProductDataModel(

    val name : String = "",
    val brand : String = "",
    val description : String = "",
    val actual_price : Int = 0,
    val offer_price : Int = 0,
    val category : String = "",
    val image : String = "",
    val rating : Float = 0f,
    val percentageOff : Float = 0f,
    val date : Long = System.currentTimeMillis(),
    val availableUnits : Int = 0,
    @PropertyName("available") val isAvailable : Boolean = true,
    var productID : String? = ""

)

