package com.example.myshoppingappuser.domain_layer.models

data class Category(

    var name : String = "",
    var date : Long = System.currentTimeMillis(),
    var image : String = ""

)
