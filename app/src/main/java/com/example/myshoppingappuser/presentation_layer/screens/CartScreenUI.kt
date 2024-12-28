package com.example.myshoppingappuser.presentation_layer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel

@Composable
fun CartScreenUI(modifier: Modifier = Modifier,navController: NavController) {

    val viewModel = hiltViewModel<MyViewModel>()
    val state = viewModel.cartProducts.collectAsStateWithLifecycle()

    val brush = Brush.linearGradient(
        listOf(
            Color.Red,
            Color.Blue
        )
    )

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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.value.error)
            }
        }

        state.value.data.isNotEmpty() -> {

            LazyColumn (
                modifier = Modifier.padding(top = 16.dp)
            ){
                items(state.value.data ?: emptyList()) {

                    Card(
                      modifier = Modifier.fillMaxWidth().padding(
                          horizontal = 16.dp,
                          vertical = 8.dp
                      )
                    ) {
                        Column (
                            modifier = Modifier.fillMaxSize().background(
                                brush = brush,
                                RectangleShape
                            ).padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text(text = it.productName)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = it.productPrice)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = it.productQty)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = it.productCategory)
                        }
                    }
                }
            }
        }
    }
}