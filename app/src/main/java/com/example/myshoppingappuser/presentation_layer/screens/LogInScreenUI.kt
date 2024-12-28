package com.example.myshoppingappuser.presentation_layer.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material.icons.rounded.VpnKeyOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshoppingappuser.common.MultiColorText
import com.example.myshoppingappuser.presentation_layer.navigation.Routes
import com.example.myshoppingappuser.presentation_layer.navigation.Routes.SignUpScreen
import com.example.myshoppingappuser.presentation_layer.view_model.MyViewModel

@Composable
fun LogInScreenUI (modifier: Modifier = Modifier,viewModel: MyViewModel = hiltViewModel(),navController: NavController) {

    val uiState = viewModel.logInUserState.collectAsState()

    val email = remember { mutableStateOf("") }

    val password = remember { mutableStateOf("") }

    when {
        uiState.value.isLoading -> {
            CircularProgressIndicator()
        }

        uiState.value.error.isNotEmpty() -> {
            Log.d("TAG", "LogInScreenUI: ${uiState.value.error}")
            DefaultFunc(navController = navController)
        }

        uiState.value.userData.isNotEmpty() -> {
            Log.d("TAG", "LogInScreenUI: ${uiState.value.userData} is successful")
            navController.navigate(Routes.HomeScreen)
        }

        else -> {
            DefaultFunc(navController = navController)
        }
    }
}

@Composable
fun DefaultFunc(viewModel : MyViewModel = hiltViewModel(), email : MutableState<String> = remember { mutableStateOf("") }, password: MutableState<String> = remember { mutableStateOf("") } , navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = { Text(text = "email") }
        )

        val visibility = remember { mutableStateOf(false) }

        val icon = if (visibility.value) {
            Icons.Rounded.RemoveRedEye
        } else {
            Icons.Rounded.VpnKeyOff
        }
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            label = { Text(text = "password") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        visibility.value = !visibility.value
                    }
                ) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            visualTransformation =
            if (visibility.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }
        )
        Button(
            onClick = {
                viewModel.logInUser(email = email.value, userPassword = password.value)
            }
        ) {
            Text(text = "Log In")
        }

        Spacer(modifier = Modifier.size(20.dp))

        MultiColorText("Don't have an account? ", "SignUp" , modifier = Modifier.clickable{
            navController.navigate(Routes.SignUpScreen){
                navController.navigateUp()
                }
            }
        )
    }
}