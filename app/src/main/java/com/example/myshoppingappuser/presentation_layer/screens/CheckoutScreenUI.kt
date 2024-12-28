package com.example.myshoppingappuser.presentation_layer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshoppingappuser.MainActivity
import com.example.myshoppingappuser.presentation_layer.navigation.Routes
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CheckoutScreenUI(navController: NavController, productId : String , firebaseAuth : FirebaseAuth) {

    val viewModel = hiltViewModel<MyViewModel>()

    val productState = viewModel.getProductByIdState.collectAsState()
    val userState = viewModel.getUserByIdState.collectAsState()

    val context = LocalContext.current

    val brush1 = Brush.linearGradient(
        listOf(
            Color(0xFFD20A1D),
            Color(0xFFB22BB0),
            Color(0xFF1555B7)
        )
    )

    val activity = context as? MainActivity

    LaunchedEffect(Unit) {
        viewModel.getProductById(productId)
        viewModel.getUserById(firebaseAuth.currentUser!!.uid)
    }

    when{
        productState.value.isLoading || userState.value.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center) , Color.Red)
            }
        }

        productState.value.error.isNotEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = productState.value.error)
            }
        }

        userState.value.error.isNotEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = userState.value.error)
            }
        }

        productState.value.data != null && userState.value.data != null -> {

            LazyColumn {

                item{

                    Button(
                        onClick = {
                            activity?.startPayment(
                                productState.value.data!!.name,
                                productState.value.data!!.offer_price
                            )
                            navController.navigate(Routes.PaymentScreen(productId))
                        }
                    ) {
                        Text(text = "Continue Shipping")
                    }
                }
            }
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Something went wrong")
            }
        }
    }
}