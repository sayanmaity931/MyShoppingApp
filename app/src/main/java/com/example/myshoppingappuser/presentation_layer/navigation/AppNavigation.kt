package com.example.myshoppingappuser.presentation_layer.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.myshoppingappuser.presentation_layer.screens.AllProductsUI
import com.example.myshoppingappuser.presentation_layer.screens.CartScreenUI
import com.example.myshoppingappuser.presentation_layer.screens.CheckoutScreenUI
import com.example.myshoppingappuser.presentation_layer.screens.EachProductDetailsScreenUI
import com.example.myshoppingappuser.presentation_layer.screens.HomeScreenUI
import com.example.myshoppingappuser.presentation_layer.screens.LogInScreenUI
import com.example.myshoppingappuser.presentation_layer.screens.PaymentScreenUI
import com.example.myshoppingappuser.presentation_layer.screens.ProfileScreenUI
import com.example.myshoppingappuser.presentation_layer.screens.SignUpScreenUI
import com.example.myshoppingappuser.presentation_layer.screens.WishListScreenUI
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(modifier: Modifier = Modifier,firebaseAuth: FirebaseAuth) {

    val navController = rememberNavController()

    val brush1 = Brush.linearGradient(
        listOf(
            Color(0xFFD20A1D),
            Color(0xFFB22BB0),
            Color(0xFF1555B7)
        )
    )

    val brush2 = Brush.linearGradient(
        listOf(
            Color.Red,
            Color.Blue
        )
    )

    val items = listOf(

        BottomNavigationItems("Home", Icons.Filled.Home , Icons.Outlined.Home),
        BottomNavigationItems("Favorite",Icons.Filled.HeartBroken, Icons.Outlined.HeartBroken),
        BottomNavigationItems("Cart",Icons.Filled.ShoppingCart , Icons.Outlined.ShoppingCart),
        BottomNavigationItems("Profile",Icons.Filled.Person2 , Icons.Outlined.Person2)

    )

    var selectedItemIndex = remember { mutableIntStateOf(0) }

    val currentDestinationAsState = navController.currentBackStackEntryAsState()

    var currentDestination = currentDestinationAsState.value?.destination?.route

    val shouldShowBottomBar = remember { mutableStateOf(false) }

//    LaunchedEffect(currentDestination) {
//        shouldShowBottomBar.value = when(currentDestination){
//            Routes.LogInScreen::class.qualifiedName, Routes.SignUpScreen::class.qualifiedName -> false
//            else -> true
//        }
//    }

    LaunchedEffect(currentDestination) {
        shouldShowBottomBar.value = when (currentDestination) {
            Routes.HomeScreen::class.qualifiedName,
            Routes.WishListScreen::class.qualifiedName,
            Routes.CartScreen::class.qualifiedName,
            Routes.ProfileScreen::class.qualifiedName -> true
            else -> false
        }
    }

    var startScreen = if(firebaseAuth.currentUser == null){
        SubNavigation.LogInSignUpScreen
    }
    else{
        SubNavigation.MainHomeScreen
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomBar.value){
            NavigationBar(
                modifier = Modifier.fillMaxWidth().background(brush2),
                containerColor = Color(0xFF350267)
            ) {
                items.forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
                        selected = selectedItemIndex.intValue == index,
                        onClick = {
                            selectedItemIndex.intValue = index

                            when (selectedItemIndex.intValue) {

                                0 -> navController.navigate(Routes.HomeScreen)
                                1 -> navController.navigate(Routes.WishListScreen)
                                2 -> navController.navigate(Routes.CartScreen)
                                3 -> navController.navigate(Routes.ProfileScreen)

                            }
                        },
                        icon = {
                            Icon(if (selectedItemIndex.intValue == index) navigationItem.selectedIcon else navigationItem.unselectedIcon , contentDescription = navigationItem.title )
                            }
                        )
                    }
                }
            }
        },
        containerColor = Color.Red
    ){innerPadding ->

        Box(modifier = modifier
            .padding(
                bottom = if (shouldShowBottomBar.value) {
                    innerPadding.calculateBottomPadding()
                } else 0.dp
            )
            .fillMaxSize()
            .background(
                brush = brush1
            )){

            NavHost(navController = navController, startDestination = startScreen){

                navigation<SubNavigation.LogInSignUpScreen>(startDestination = Routes.LogInScreen){

                    composable<Routes.LogInScreen>{
                        LogInScreenUI(navController = navController)
                    }

                    composable<Routes.SignUpScreen>{
                        SignUpScreenUI(navController = navController)
                    }

                }

                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen ){

                    composable<Routes.HomeScreen>{
                        HomeScreenUI(navController = navController , firebaseAuth = firebaseAuth)
                    }

                    composable<Routes.ProfileScreen>{
                        ProfileScreenUI(navController = navController , firebaseAuth = firebaseAuth)
                    }

                    composable<Routes.WishListScreen>{
                        WishListScreenUI(navController)
                    }

                    composable<Routes.CartScreen>{
                        CartScreenUI(navController = navController)
                    }

                }

                composable<Routes.EachProductDetailScreen>{
                    val data = it.toRoute<Routes.EachProductDetailScreen>()
                    EachProductDetailsScreenUI(
                        navController = navController,
                        productId = data.productId
                    )
                }

                composable<Routes.SeeAllProductScreen>{
                    AllProductsUI(firebaseAuth = firebaseAuth , navController = navController)
                }

                composable<Routes.CheckoutScreen>{
                    val data = it.toRoute<Routes.CheckoutScreen>()
                    CheckoutScreenUI(
                        navController = navController, productId = data.productId,
                        firebaseAuth = firebaseAuth
                    )
                }

                composable<Routes.PaymentScreen>{
                    val data = it.toRoute<Routes.PaymentScreen>()
                    PaymentScreenUI(
                        navController = navController, productId = data.productId,
                        firebaseAuth = firebaseAuth
                    )
                }
            }
        }
    }
}

data class BottomNavigationItems(val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector)