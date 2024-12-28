package com.example.myshoppingappuser.domain_layer.models

data class UserData(

    var firstName : String = "",
    var lastName : String = "",
    var email : String = "",
    val password : String = "",
    var phoneNumber : String = "",
    var address : String = "",
    val userImage : String = "",
    var uid : String = ""

)
