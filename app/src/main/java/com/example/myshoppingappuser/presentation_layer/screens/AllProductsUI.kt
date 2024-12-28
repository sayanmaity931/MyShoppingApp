package com.example.myshoppingappuser.presentation_layer.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.contentReceiver
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshoppingappuser.R
import com.example.myshoppingappuser.domain_layer.models.ProductDataModel
import com.example.myshoppingappuser.presentation_layer.navigation.Routes
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AllProductsUI(firebaseAuth : FirebaseAuth , navController : NavController) {

    val viewModel = hiltViewModel<MyViewModel>()

    val state = viewModel.getAllProductsState.collectAsState()

    val likedProducts = viewModel.likedProducts.collectAsState()

    val liked = viewModel.likeAddedResponse.collectAsState()

    val brush2 = Brush.linearGradient(
        listOf(
            Color.Red,
            Color.Blue
        )
    )

    val brush1 = Brush.linearGradient(
        listOf(
            Color(0xFFD20A1D),
            Color(0xFFB22BB0),
            Color(0xFF1555B7)
        )
    )

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    val data = state.value.data

    when{
        state.value.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), Color.Red)
            }
        }

        state.value.error.isNotBlank() -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Text(text = state.value.error)
            }
        }

        liked.value.error.isNotBlank() -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Text(text = liked.value.error)
            }
        }

        state.value.data.isNotEmpty() && liked.value.data.isNotEmpty() || liked.value.isLoading -> {

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.padding(start = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "See More",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "See Your Favourite \n         One",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W400,
                                    color = Color.Red,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(6.dp)
                                        .clickable(
                                            enabled = true,
                                            onClick = {}
                                        )
                                )
                            }
                            Image(
                                painter = painterResource(id = R.drawable.ellipse1),
                                contentDescription = null,
                                modifier = Modifier.size(250.dp, 250.dp)
                            )
                        }
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text(text = "Search For Product") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null
                                )
                            },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            shape = Shapes().extraLarge,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Red,
                                focusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 25.dp, end = 25.dp)
                                .height(3.5.dp)
                        ) {
                            Color.Black
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                            LazyColumn {

                                items(data) {

                                    val isLiked = likedProducts.value.contains(it.productID)

                                    val iconValue =
                                        remember { mutableStateOf(if (isLiked) Icons.Rounded.ThumbUp else Icons.Outlined.ThumbUp) }

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                            .clickable(
                                                enabled = true,
                                                onClick = {
                                                    navController.navigate(
                                                        Routes.EachProductDetailScreen(
                                                            productId = it.productID.toString()
                                                        )
                                                    )
                                                }
                                            )
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().background(brush2),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.frock1),
                                                contentDescription = null,
                                                modifier = Modifier.size(200.dp, 200.dp)
                                            )
                                            Column {

                                                Text(text = "Name : ${it.name}")
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = "Brand : ${it.brand}")
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = "Offer Price : ${it.offer_price}")
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = "Actual Price : ${it.actual_price}")
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = "Percentage Off : ${it.percentageOff}")
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = "Rating : ${it.rating}")
                                                Spacer(modifier = Modifier.height(4.dp))

                                                Box(contentAlignment = Alignment.Center) {

                                                    IconButton(
                                                        onClick = {
                                                            if (isLiked) {
                                                                viewModel.removeLikedProduct(
                                                                    it.productID.toString(),
                                                                    firebaseAuth.currentUser?.uid
                                                                        ?: ""
                                                                )
                                                                iconValue.value =
                                                                    Icons.Outlined.ThumbUp
                                                            } else {
                                                                viewModel.addLikedProduct(
                                                                    it.productID.toString(),
                                                                    firebaseAuth.currentUser?.uid
                                                                        ?: ""
                                                                )
                                                                iconValue.value =
                                                                    Icons.Rounded.ThumbUp
                                                            }
                                                        }
                                                    ) {
                                                        if (isLiked) {
                                                            Icon(
                                                                imageVector = Icons.Rounded.ThumbUp,
                                                                contentDescription = null
                                                            )
                                                        } else {
                                                            Icon(
                                                                imageVector = Icons.Outlined.ThumbUp,
                                                                contentDescription = null
                                                            )
                                                        }
                                                        Log.d(
                                                            "UI",
                                                            "Icon for product ${it.productID}: ${if (isLiked) "Liked" else "Not Liked"}"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Error")
            }
        }
    }
}
