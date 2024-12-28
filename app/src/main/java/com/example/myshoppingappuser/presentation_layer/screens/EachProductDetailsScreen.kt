package com.example.myshoppingappuser.presentation_layer.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myshoppingappuser.domain_layer.models.CartModel
import com.example.myshoppingappuser.presentation_layer.navigation.Routes
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun EachProductDetailsScreenUI(modifier: Modifier = Modifier,viewModel: MyViewModel = hiltViewModel() , navController: NavController , productId : String) {

    val state = viewModel.getProductByIdState.collectAsState().value
    val context = LocalContext.current
    val flow = viewModel.isAdded.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d("EachProductDetails", "Product ID: $productId")
        viewModel.getProductById(productId)
        viewModel.isAddedToCart(productId)
        Log.d("value1",flow.value.toString())
    }

    when{
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = modifier.align(Alignment.Center), Color.Red)
            }
        }
        state.error.isNotBlank() -> {
            Text(text = state.error)
        }
        state.data != null -> {
            Column (
                modifier = modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(text = "Product Name : ${state.data.name}")
                Spacer(modifier = modifier.padding(10.dp))
                Text(text = "Available Units : ${state.data.availableUnits}")
                Spacer(modifier = modifier.padding(10.dp))
                Text(text = "Offer Price : ${state.data.offer_price}")
                Spacer(modifier = modifier.padding(10.dp))
                Button(
                    onClick = {
                        viewModel.removeCapacity(state.data.productID.toString() , (state.data.availableUnits-1))
                        navController.navigate(Routes.CheckoutScreen(state.data.productID.toString()))
                        Log.d("EachProductDetails", "Product#: ${viewModel._removeCapacity}")
                    }
                ) {
                    Text(text = "Buy Now")
                }
                Spacer(modifier = modifier.padding(10.dp))
                if (!flow.value) {

                    Button(
                        onClick = {
                            viewModel.addToCart(
                                CartModel(
                                    productId = state.data.productID.toString(),
                                    productName = state.data.name,
                                    productPrice = state.data.actual_price.toString(),
                                    productQty = state.data.availableUnits.toString(),
                                    productFinalPrice = state.data.offer_price.toString(),
                                    productCategory = state.data.category
                                )
                            )
                            Toast.makeText(context, "Product Added To Cart", Toast.LENGTH_SHORT)
                                .show()
                        }
                    ) {
                        Text(text = "Add To Cart")
                    }
                }else {
                    Button(
                        onClick = {
                            viewModel.addToCart(
                                CartModel(
                                    productId = state.data.productID.toString(),
                                    productName = state.data.name,
                                    productPrice = state.data.actual_price.toString(),
                                    productQty = state.data.availableUnits.toString(),
                                    productFinalPrice = state.data.offer_price.toString(),
                                    productCategory = state.data.category
                                )
                            )
                            Toast.makeText(context, "Product Removed From Cart", Toast.LENGTH_SHORT)
                                .show()
                        }
                    ) {
                        Text(text = "Remove From Cart")
                    }
                }
            }
        }
    }
}