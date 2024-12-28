package com.example.myshoppingappuser.presentation_layer.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PaymentScreenUI(modifier: Modifier = Modifier , navController: NavController ,productId : String , firebaseAuth : FirebaseAuth) {

    val viewModel = hiltViewModel<MyViewModel>()


}