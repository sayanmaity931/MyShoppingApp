package com.example.myshoppingappuser.presentation_layer.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation{

    @Serializable
    object MainHomeScreen : SubNavigation()

    @Serializable
    object LogInSignUpScreen : SubNavigation()
    
}

sealed class Routes {

    @Serializable
    object LogInScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object HomeScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object WishListScreen

    @Serializable
    object CartScreen

    @Serializable
    data class CheckoutScreen(
        val productId: String
    )

    @Serializable
    data class PaymentScreen(
        val productId: String
    )

    @Serializable
    object SeeAllProductScreen

    @Serializable
    data class EachProductDetailScreen(
        val productId : String
    )

    @Serializable
    object AllCategoryScreen

    @Serializable
    object EachCategoryItemScreen

}